package com.marcotte.microservices.core.review.businesslayer;

import com.marcotte.api.core.review.Review;

import java.util.List;

public interface ReviewService {

    public List<Review> getProductById(int productId);

    public Review createReview(Review model);

    public void deleteReviews(int productId);

}
