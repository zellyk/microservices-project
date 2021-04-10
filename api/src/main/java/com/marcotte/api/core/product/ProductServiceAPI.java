package com.marcotte.api.core.product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProductServiceAPI {

    @GetMapping(
            value = "/product/{productId}",
            produces = "application/json"
    )
    Product getProduct(@PathVariable int productId);
}
