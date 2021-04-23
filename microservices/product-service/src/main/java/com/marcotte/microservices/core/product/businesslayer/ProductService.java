package com.marcotte.microservices.core.product.businesslayer;


import com.marcotte.api.core.product.Product;
import org.springframework.context.annotation.ComponentScan;


public interface ProductService {

    public Product getProductById(int productId);

    public Product createProduct(Product model);

    public void deleteProduct(int productId);

}
