package com.marcotte.microservices.core.recommendation.presentationlayer.controllers;

import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.api.core.recommendation.RecommendationServiceAPI;
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

    private final ServiceUtil serviceUtil;

    public RecommendationRESTController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {

        if(productId < 1 )throw new InvalidInputException("invalid productId:" + productId);
        if(productId == 113)
        {
            LOG.debug("No recommendations found for productId: {}", + productId);
            return new ArrayList<>();
        }

        List<Recommendation> listRecommendations = new ArrayList<>();

        listRecommendations.add(new Recommendation(productId, 1, "Author 1", 1, "Content 1", serviceUtil.getServiceAddress()));
        listRecommendations.add(new Recommendation(productId, 2, "Author 2", 1, "Content 2", serviceUtil.getServiceAddress()));
        listRecommendations.add(new Recommendation(productId, 3, "Author 3", 1, "Content 3", serviceUtil.getServiceAddress()));

        LOG.debug("/recommendations found for response size: {}", listRecommendations.size());

        return listRecommendations;
    }
}
