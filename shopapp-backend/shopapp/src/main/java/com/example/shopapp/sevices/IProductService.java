package com.example.shopapp.sevices;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long productId) throws Exception;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long id,ProductDTO productDTO) throws Exception;
    void deleteProduct(long productId);
    boolean existsByName(String name);
    ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception;
}
