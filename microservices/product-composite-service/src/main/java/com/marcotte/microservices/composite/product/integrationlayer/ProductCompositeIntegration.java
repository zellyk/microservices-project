package com.marcotte.microservices.composite.product.integrationlayer;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcotte.api.core.product.Product;
import com.marcotte.api.core.product.ProductServiceAPI;
import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.api.core.recommendation.RecommendationServiceAPI;
import com.marcotte.api.core.review.Review;
import com.marcotte.api.core.review.ReviewServiceAPI;
import com.marcotte.utils.exceptions.InvalidInputException;
import com.marcotte.utils.exceptions.NotFoundException;
import com.marcotte.utils.http.HttpErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductCompositeIntegration implements ProductServiceAPI, RecommendationServiceAPI, ReviewServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    public ProductCompositeIntegration(RestTemplate restTemplate,
                                       ObjectMapper mapper,
                                       @Value("${app.product-service.host}") String productServiceHost,
                                       @Value("${app.product-service.port}") String productServicePort,
                                       @Value("${app.recommendation-service.host}") String recommendationServiceHost,
                                       @Value("${app.recommendation-service.port}") String recommendationServiceport,
                                       @Value("${app.review-service.host}") String reviewServiceHost,
                                       @Value("${app.review-service.port}") String reviewServiceport
                                     ) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;

        productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";

        // recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServiceport + "/recommendation?productId=";
        recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServiceport + "/recommendation/";

        // reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServiceport + "/review?productId=";
        reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServiceport + "/review/";
    }

    @Override
    public Product getProduct(int productId) {  // sends GET request to product service
        try{
            String url= productServiceUrl + productId;
            LOG.debug("will call getProduct API on URL: {}", url);

            Product product = restTemplate.getForObject(url, Product.class);
            LOG.debug("Found a Product with id: {}", product.getProductId());

            return product;
        }
        catch (HttpClientErrorException ex)
        {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public Product createProduct(Product model) { // POST request to product service

        try
        {
           return restTemplate.postForObject(productServiceUrl, model, Product.class);
        }
        catch (HttpClientErrorException ex)
        {
            throw handleHttpClientException(ex);
        }
    }


    @Override
    public void deleteProduct(int productId) {

        try
        {
            String url = productServiceUrl + productId;
            LOG.debug("Will call the deleteProduct API on URL: ()", url);

            restTemplate.delete(url);
        }
        catch (HttpClientErrorException ex)
        {
            throw handleHttpClientException(ex);
        }

    }

    private String getErrorMessage(HttpClientErrorException ex) {
        try{
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        }
        catch(IOException ioex){
            return ioex.getMessage();
        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) { // sends GET by productId request to recommendation service
        try{
            String url = recommendationServiceUrl + "?productId=" + productId;

            LOG.debug("Will call getRecommendations API on URL: {}", url);
            List<Recommendation> recommendations = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Recommendation>>() {
                    }).getBody();
            LOG.debug("Found {} recommendations for product with id: {}", recommendations.size(), productId);
            return recommendations;
        }
        catch(Exception ex){
            LOG.warn("Got exception while requesting recommendations, returns zero recommendations: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public Recommendation createRecommendation(Recommendation model) {

        try
        {
            String url = recommendationServiceUrl;
            LOG.debug("Will post a new recommendation to URL: {}", url);

            Recommendation recommendation = restTemplate.postForObject(url, model, Recommendation.class);
            LOG.warn("Created a new recommendation for productId: {}, recommendatioId: {}", recommendation.getProductId(), recommendation.getRecommendationId());

            return recommendation;

        }
        catch (HttpClientErrorException ex)
        {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public void deleteRecommendation(int productId) { // sed a DELETE by productId tp recommendation service

        try
        {
            String url = recommendationServiceUrl + "?productId=" + productId;
            LOG.debug("Will call deleteRecommendation to URL: {}", url);

            restTemplate.delete(url);
        }
        catch (HttpClientErrorException ex)
        {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public List<Review> getReviews(int productId) { // sends GET by productId request to recommendation service
        try{
                String url = reviewServiceUrl + "?productId=" + productId;

                LOG.debug("Will call getReviews API on URL: {}", url);
                List<Review> reviews = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Review>>() {
                    }).getBody();
            LOG.debug("Found {} reviews for product with id: {}", reviews.size(), productId);
            return reviews;
        }
        catch(Exception ex){
            LOG.warn("Got exception while requesting reviews, returns zero reviews: {}", ex.getMessage());
            return new ArrayList<>();
        }

    }

    @Override
    public Review createReview(Review model) {
        try{
            String url = reviewServiceUrl;
            LOG.debug("Will post a new review to URL: {}", url);

            Review review = restTemplate.postForObject(url, model, Review.class);
            LOG.debug("Will post a new review for productId: {}, reviewId: {}", review.getProductId(), review.getProductId());

            return review;
        }
        catch (HttpClientErrorException ex)
        {
            throw handleHttpClientException(ex);
        }
    }

    @Override
    public void deleteReviews(int productId) {
        try
        {
            String url = reviewServiceUrl + "?productId=" + productId;
            LOG.debug("Will call deleteReview to URL: {}", url);

            restTemplate.delete(url);
        }
        catch (HttpClientErrorException ex)
        {
            throw handleHttpClientException(ex);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        switch (ex.getStatusCode()) {        case NOT_FOUND:
            return new NotFoundException(getErrorMessage(ex));        case UNPROCESSABLE_ENTITY :
            return new InvalidInputException(getErrorMessage(ex));        default:
            LOG.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
            LOG.warn("Error body: {}", ex.getResponseBodyAsString());
            return ex;
        }
    }

}
