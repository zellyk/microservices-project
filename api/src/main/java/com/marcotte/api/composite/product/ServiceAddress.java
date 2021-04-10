package com.marcotte.api.composite.product;

public class ServiceAddress {
    private String compositeAddress;
    private String productAddress;
    private String reviewAddress;
    private String recommendationAddress;

    public ServiceAddress(String compositeAddress, String productAddress, String reviewAddress, String recommendationAddress) {
        this.compositeAddress = compositeAddress;
        this.productAddress = productAddress;
        this.reviewAddress = reviewAddress;
        this.recommendationAddress = recommendationAddress;
    }

    public String getCompositeAddress() {
        return this.compositeAddress;
    }

    public String getProductAddress() {
        return this.productAddress;
    }

    public String getReviewAddress() {
        return this.reviewAddress;
    }

    public String getRecommendationAddress() {
        return this.recommendationAddress;
    }
}
