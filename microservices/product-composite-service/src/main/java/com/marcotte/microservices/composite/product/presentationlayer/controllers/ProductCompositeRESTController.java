package com.marcotte.microservices.composite.product.presentationlayer.controllers;


import com.marcotte.api.composite.product.*;
import com.marcotte.api.core.product.Product;
import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.api.core.review.Review;
import com.marcotte.microservices.composite.product.businesslayer.ProductCompositeService;
import com.marcotte.microservices.composite.product.businesslayer.ProductCompositeServiceImpl;
import com.marcotte.microservices.composite.product.integrationlayer.ProductCompositeIntegration;
import com.marcotte.utils.exceptions.NotFoundException;
import com.marcotte.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductCompositeRESTController implements ProductCompositeServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private final ProductCompositeService productCompositeService;

    public ProductCompositeRESTController(ProductCompositeService productCompositeService) {
        this.productCompositeService = productCompositeService;
    }

    @Override
    public ProductAggregate getProduct(int productId) {

        LOG.debug("ProductComposite received getProductComposite request.");

        ProductAggregate productAggregate = productCompositeService.getProduct(productId);

        return productAggregate;
    }

    @Override
    public void createCompositeProduct(ProductAggregate model) {

        LOG.debug("ProductComposite received dreateProductComposite request.");

        productCompositeService.createProduct(model);
    }

    @Override
    public void deleteCompositeProduct(int productId) {
        LOG.debug("ProductComposite received deleteProductComposite request.");
        productCompositeService.deleteProduct(productId);
    }

}
