package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.dtos.ProductDTO;
import com.huyhaf.shopapp.dtos.ProductImageDTO;
import com.huyhaf.shopapp.exceptions.DataNotFoundException;
import com.huyhaf.shopapp.exceptions.InvalidParamException;
import com.huyhaf.shopapp.models.Category;
import com.huyhaf.shopapp.models.Product;
import com.huyhaf.shopapp.models.ProductImage;
import com.huyhaf.shopapp.repositories.CategoryRepository;
import com.huyhaf.shopapp.repositories.ProductImageRepository;
import com.huyhaf.shopapp.repositories.ProductRepository;
import com.huyhaf.shopapp.responses.ProductResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO) throws Exception {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id:" + productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    @Cacheable(value = "products", key = "#productId")
    // Lần đầu gọi, code này sẽ chạy và kết quả được lưu vào cache
    // Lần sau gọi với cùng productId, kết quả được trả về từ Redis, không chạy code bên dưới
    public Product getProductById(long productId) throws Exception {
        return productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id:"+productId));
    }

    @Override
    public Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest) {
        Page<Product> productsPage = productRepository.searchProducts(categoryId, keyword, pageRequest);
        return productsPage.map(ProductResponse::fromProduct);
    }

    @Override
    @Transactional
    @CachePut(value = "products", key = "#id") // Cập nhật lại cache khi sửa
    public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
        Product existingProduct;
        try {
            existingProduct = getProductById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (existingProduct!=null){
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException("Cannot find category with id:" + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            return productRepository.save(existingProduct); // Giá trị trả về sẽ ghi đè cache cũ
        }
        return null;
    }

    @Override
    @Transactional
    @CacheEvict(value = "products", key = "#productId") // Xóa cache khi xóa
    public void deleteProduct(long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    @Transactional
    public ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Cannot find category with id:" + productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        // ko cho insert 5 anh cho 1 san pham
        int size = productImageRepository.findByProductId(existingProduct.getId()).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamException("Number of images must be <= "+ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }
}
