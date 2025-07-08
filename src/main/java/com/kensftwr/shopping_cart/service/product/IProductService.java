package com.kensftwr.shopping_cart.service.product;

import java.util.List;

import com.kensftwr.shopping_cart.dtos.ProductRequest;
import com.kensftwr.shopping_cart.dtos.ProductResponse;
import com.kensftwr.shopping_cart.models.Product;

public interface IProductService {

    ProductResponse addProduct(ProductRequest productRequest);

    List<Product> getAllProducts();

    Product getProductById(Long id);

    void deleteProductById(Long id);

    ProductResponse updateProduct(ProductRequest productRequest, Long productId);

    List<Product> getProductByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String category, String name);

    Long countProductsByBrandAndName(String brand, String name);

}



// Typescript equivalent
// export interface IProductService {
//     addProduct(product: Product): Product;
  
//     getAllProducts(): Product[];
  
//     getProductById(id: number): Product;
  
//     deleteProductById(id: number): void;
  
//     updateProduct(product: Product, productId: number): void;
  
//     getProductByCategory(category: string): Product[];
  
//     getProductsByBrand(brand: string): Product[];
  
//     getProductsByCategoryAndBrand(category: string, brand: string): Product[];
  
//     getProductsByName(name: string): Product[];
  
//     getProductsByBrandAndName(brand: string, name: string): Product[];
  
//     countProductsByBrandAndName(brand: string, name: string): number;
//   }
