package com.marcotte.api.core.recommendation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface RecommendationServiceAPI {

    @GetMapping(
            value = "/recommendation",
            produces = "application/json"
    )
    List<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId);
}
