package com.marcotte.microservices.composite.product.presentationlayer.controllers;


import com.marcotte.api.composite.product.*;
import com.marcotte.api.core.product.Product;
import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.api.core.review.Review;
import com.marcotte.microservices.composite.product.integrationlayer.ProductCompositeIntegration;
import com.marcotte.utils.exceptions.NotFoundException;
import com.marcotte.utils.http.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductCompositeRESTController implements ProductCompositeServiceAPI {

    private final ServiceUtil serviceUtil;
    private final ProductCompositeIntegration integration;

    public ProductCompositeRESTController(ServiceUtil serviceUtil, ProductCompositeIntegration integration) {
        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public ProductAggregate getProduct(int productId) {
        Product product = integration.getProduct(productId);
        if(product == null) throw new NotFoundException("No product found for productId: {}" + productId);

        List<Recommendation> recommendations = integration.getRecommendations(productId);

        List<Review> reviews = integration.getReviews(productId);

        return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
    }

    private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations, List<Review> reviews, String serviceAddress){

        // 1. Setup product information
        int productId = product.getProductId();
        String name = product.getName();
        int weight = product.getWeight();

        // 2. Copy summary recommendation info, if any
        List<RecommendationSummary> recommendationSummaries = (recommendations == null) ? null :
                recommendations.stream()
                        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
                        .collect(Collectors.toList());

        // 3. Copy summary reviews info, if any
        List<ReviewSummary> reviewSummaries = (reviews == null) ? null :
                reviews.stream()
                        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
                        .collect(Collectors.toList());

        // 4. Create info for Microservice Address
        String productAddress = product.getServiceAddress();
        String recommendationAddress = (recommendations != null && recommendations.size() > 0)
                ? recommendations.get(0).getServiceAddress() : "";
        String reviewAddress = (reviews != null && reviews.size() > 0)
                ? reviews.get(0).getServiceAddress() : "";

        ServiceAddress serviceAddresses = new ServiceAddress(serviceAddress, productAddress, reviewAddress, recommendationAddress);

        return new ProductAggregate(productId, name, weight, recommendationSummaries, reviewSummaries, serviceAddresses);
    }





}
