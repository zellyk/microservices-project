package com.marcotte.api.core.product;

import org.springframework.web.bind.annotation.*;

public interface ProductServiceAPI {

    @GetMapping(
            value = "/product/{productId}",
            produces = "application/json"
    )
    Product getProduct(@PathVariable int productId);

    @PostMapping(
            value = "/product",
            consumes = "application/json",
            produces =  "application/json"
    )
    Product createProduct(@RequestBody Product model);

    @DeleteMapping(value = "/product/{productId}")
    void deleteProduct(@PathVariable int productId);
}
