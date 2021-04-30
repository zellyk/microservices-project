package com.marcotte.microservices.core.review.presentationlayer.controllers;

import com.marcotte.api.core.review.Review;
import com.marcotte.api.core.review.ReviewServiceAPI;
import com.marcotte.microservices.core.review.businesslayer.ReviewService;
import com.marcotte.utils.exceptions.InvalidInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewRESTController implements ReviewServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewRESTController.class);

    private final ReviewService reviewService;

    public ReviewRESTController(ReviewService reviewService){
        this.reviewService = reviewService;

    }

    @Override
    public List<Review> getReviews(int productId) {

        if(productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        List<Review> listReviews = reviewService.getProductById(productId);

        LOG.debug("/getReviews found response size: {}", listReviews.size());

        return listReviews;
    }

    @Override
    public Review createReview(Review model) {
        Review review = reviewService.createReview(model);

        LOG.debug("REST Controller createReview: created an entity: {} / {}",review.getProductId(),review.getReviewId());

        return review;
    }

    @Override
    public void deleteReviews(int productId) {

        LOG.debug("REST Controller deleteReview: trying to delete reviews for the product with productId: {}", productId);
        reviewService.deleteReviews(productId);

    }
}