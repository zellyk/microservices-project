package com.marcotte.microservices.core.product.presentationlayer.controllers;


import com.marcotte.api.core.product.Product;
import com.marcotte.api.core.product.ProductServiceAPI;
import com.marcotte.utils.exceptions.InvalidInputException;
import com.marcotte.utils.exceptions.NotFoundException;
import com.marcotte.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRESTController implements ProductServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ProductRESTController.class);

    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductRESTController(ServiceUtil serviceUtil)
    {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        LOG.debug("/product MS returns the found product for productId: " + productId);

        if(productId < 1)throw new InvalidInputException("Invalid productId: " + productId);
        if(productId == 13)throw new NotFoundException("No product found for productId: " + productId);


        return new Product(productId, "name: "+ productId, 123, serviceUtil.getServiceAddress());
    }
}