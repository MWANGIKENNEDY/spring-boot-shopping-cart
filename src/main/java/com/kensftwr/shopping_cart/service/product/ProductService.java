package com.kensftwr.shopping_cart.service.product;

import java.util.List;

import com.kensftwr.shopping_cart.dtos.CategoryResponse;
import com.kensftwr.shopping_cart.dtos.ImageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kensftwr.shopping_cart.dtos.ProductRequest;
import com.kensftwr.shopping_cart.dtos.ProductResponse;
import com.kensftwr.shopping_cart.exceptions.ProductNotFoundException;
import com.kensftwr.shopping_cart.models.Category;
import com.kensftwr.shopping_cart.models.Product;
import com.kensftwr.shopping_cart.repository.CategoryRepository;
import com.kensftwr.shopping_cart.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Autowired
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        // Find existing category or create new one
        Category category = categoryRepository.findByName(productRequest.getCategory().getName())
                .orElseGet(() -> {
                    Category cat = new Category(productRequest.getCategory().getName());
                    return categoryRepository.save(cat);
                });

        productRequest.setCategory(category);
        // Map to Product and set the category
        Product product = modelMapper.map(productRequest, Product.class);

        // Save and return response
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);
    }

    // Category findByName(String name);
    // private Product createProduct(ProductRequest productRequest, Category
    // category) {
    // return new Product(
    // productRequest.getName(),
    // productRequest.getBrand(),
    // productRequest.getPrice(),
    // productRequest.getInventory(),
    // productRequest.getDescription(),
    // category);
    // }

    // public Product addNewProd(ProductRequest productRequest) {

    // Category category =
    // Optional.ofNullable(categoryRepository.findByName(productRequest.getCategory().getName()))
    // .orElseGet(() -> {
    // Category cat = new Category(productRequest.getCategory().getName());
    // return categoryRepository.save(cat);
    // });
    // productRequest.setCategory(category);

    // return productRepository.save(createProduct(productRequest, category));

    // }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(product -> {
            ProductResponse dto = new ProductResponse();
            dto.setName(product.getName());
            dto.setDescription(product.getDescription());
            dto.setPrice(product.getPrice());
            dto.setInventory(product.getInventory());
            dto.setBrand(product.getBrand());

            // ✅ set category (id + name)
            CategoryResponse categoryDTO = new CategoryResponse();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            dto.setCategory(categoryDTO);

            // ✅ map images
            List<ImageResponse> imageResponses = product.getImages().stream().map(image -> {
                ImageResponse imageDTO = new ImageResponse();
                imageDTO.setImageId(image.getId());
                imageDTO.setImageName(image.getFilename());
                imageDTO.setDownloadUrl(image.getDownloadUrl());
                return imageDTO;
            }).toList();

            dto.setImages(imageResponses);

            return dto;
        }).toList();
    }

        @Override
    public ProductResponse getProductById(Long id) {
        return productRepository.findById(id).map(e -> modelMapper.map(e,ProductResponse.class))
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
            throw new ProductNotFoundException("Product not found");
        });
    }

    @Override
    public ProductResponse updateProduct(ProductRequest productRequest, Long productId) {
        // check if the product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        // check if category exists,
        // if not create a new category
        Category category = categoryRepository.findByName(productRequest.getCategory().getName())
                .orElseGet(() -> {
                    Category cat = new Category(productRequest.getCategory().getName());
                    return categoryRepository.save(cat);
                });

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setBrand(productRequest.getBrand());
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductResponse.class);

    }

    // private Product updateExistingProduct(Product existingProduct, ProductRequest
    // productRequest) {
    // existingProduct.setName(productRequest.getName());
    // existingProduct.setBrand(productRequest.getBrand());

    // existingProduct.setPrice(productRequest.getPrice());

    // existingProduct.setInventory(productRequest.getInventory());

    // existingProduct.setDescription(productRequest.getDescription());

    // //if the category does not exist
    // //create category!!
    // Category category =
    // categoryRepository.findByName(productRequest.getCategory().getName())
    // .orElseGet(() -> {
    // Category cat = new Category(productRequest.getCategory().getName());
    // return categoryRepository.save(cat);
    // });

    // productRequest.setCategory(category);

    // existingProduct.setCategory(category);

    // return existingProduct;

    // }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);

    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {

        return productRepository.findByBrandAndName(brand, name);

    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);

    }

}
