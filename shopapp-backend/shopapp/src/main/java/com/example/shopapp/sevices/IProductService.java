package com.example.shopapp.sevices;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(long productId) throws DataNotFoundException;
    Page<Product> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long id,ProductDTO productDTO) throws DataNotFoundException;
    void deleteProduct(long productId);
    boolean existsByName(String name);
    ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception;
}
