package com.example.shopapp.controllers;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.ProductListResponse;
import com.example.shopapp.responses.ProductResponse;
import com.example.shopapp.responses.ResponseObject;
import com.example.shopapp.sevices.IProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    @PostMapping("")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ){
        try{
            if (result.hasErrors()){
                List<String> errorMessages= result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok("Product created successfully: "+newProduct);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //POST http://localhost:8088/v1/api/products
    public ResponseEntity<?> uploadImages(
            @PathVariable("id") Long productId,
            @RequestParam("files") List<MultipartFile> files
    ) throws Exception {
        Product existingProduct = productService.getProductById(productId);
        try{
            System.out.println(files.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        files = files == null ? new ArrayList<MultipartFile>() : files;
        if(files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            return ResponseEntity.badRequest().body("You can only upload maximum 5 images");
        }
        List<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile file : files) {
            if(file.getSize() == 0) {
                continue;
            }
            // Kiểm tra kích thước file và định dạng
            if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                        .body("File is too loarge");
            }
            String contentType = file.getContentType();
            if(contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                        .body("File must be an image");
            }
            // Lưu file và cập nhật thumbnail trong DTO
            String filename = storeFile(file);
            //lưu vào đối tượng product trong DB
            ProductImage productImage = productService.createProductImage(
                    existingProduct.getId(),
                    ProductImageDTO.builder()
                            .imageUrl(filename)
                            .build()
            );
            productImages.add(productImage);
        }

        return ResponseEntity.ok().body(productImages);
    }

    private String storeFile(MultipartFile file) throws IOException{
        if (!isImageFile(file) || file.getOriginalFilename() == null){
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFilename = UUID.randomUUID() + "_" + filename;
        Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        Path destination = Paths.get(uploadDir.toString(),uniqueFilename);

        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page, limit,
                Sort.by("createdAt").descending()
        );
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPages = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPages(totalPages)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById (
            @PathVariable("id") Long id
    ) {
        try {
            Product existingProduct = productService.getProductById(id);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct (@PathVariable long id){
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok("Product with id "+ id +" deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO
    ) {
        try {
            Product updatedProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i=0;i<1_000_000;i++){
            String productName = faker.commerce().productName();
            if(productService.existsByName(productName)) continue;
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float) faker.number().numberBetween(10,90_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(1,4))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok("Fake products created successfully");
    }
}
