package com.marcotte.microservices.core.recommendation.businesslayer;

import com.marcotte.api.core.recommendation.Recommendation;

import java.util.List;

public interface RecommendationService {

    public List<Recommendation> findByProductId(int productId);

    public Recommendation createRecommendation(Recommendation model);

    public void deleteRecommendations(int productId);
}