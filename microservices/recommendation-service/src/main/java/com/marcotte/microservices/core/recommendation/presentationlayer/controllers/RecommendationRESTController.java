package com.marcotte.microservices.core.recommendation.presentationlayer.controllers;

import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.api.core.recommendation.RecommendationServiceAPI;
import com.marcotte.microservices.core.recommendation.businesslayer.RecommendationService;
import com.marcotte.utils.exceptions.InvalidInputException;
import com.marcotte.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecommendationRESTController implements RecommendationServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationRESTController.class);

    private final RecommendationService recommendationService;

    private final ServiceUtil serviceUtil;

    public RecommendationRESTController(RecommendationService recommendationService, ServiceUtil serviceUtil) {
        this.recommendationService = recommendationService;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {

        if(productId < 1 )throw new InvalidInputException("Invalid productId: " + productId);
//        if(productId == 113)
//        {
//            LOG.debug("No recommendations found for productId: {}", + productId);
//            return new ArrayList<>();
//        }

          List<Recommendation> listRecommendations = recommendationService.findByProductId(productId);
          LOG.debug("Recommendations found for productId: {}", productId);
          LOG.debug("Recommendation1 productId= {}", listRecommendations.get(0).getProductId());

        return listRecommendations;
    }

    @Override
    public Recommendation createRecommendation(Recommendation model){
        Recommendation recommendation = recommendationService.createRecommendation(model);
        LOG.debug("REST Controller createRecommendation: created an entity: {} /{}", recommendation.getProductId(), recommendation.getRecommendationId());

        return recommendation;
    }

    @Override
    public void deleteRecommendation(int productId) {
        LOG.debug("REST Controller deleteRecommendation: Trying ro delete recommendations for the product with productId: {}", productId);
        recommendationService.deleteRecommendations(productId);

    }




}
