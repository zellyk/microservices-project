package com.marcotte.api.core.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
public class Product {

    private int productId;
    private String name;
    private int weight;
    private String serviceAddress;


    public Product() {
        productId = 0;
        name = null;
        weight = 0;
        serviceAddress = null;
    }
    public Product(int productId, String name, int weight, String serviceAddress) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.serviceAddress = serviceAddress;
    }
    public int getProductId() {
        return productId;
    }
    public String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }
    public String getServiceAddress() {
        return serviceAddress;
    }

}
