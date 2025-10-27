package com.huyhaf.shopapp.sevices;

import com.huyhaf.shopapp.dtos.ProductDTO;
import com.huyhaf.shopapp.dtos.ProductImageDTO;
import com.huyhaf.shopapp.models.Product;
import com.huyhaf.shopapp.models.ProductImage;
import com.huyhaf.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long productId) throws Exception;
    Page<ProductResponse> getAllProducts(String keyword, Long categoryId,PageRequest pageRequest);
    Product updateProduct(long id,ProductDTO productDTO) throws Exception;
    void deleteProduct(long productId);
    boolean existsByName(String name);
    ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception;
}
