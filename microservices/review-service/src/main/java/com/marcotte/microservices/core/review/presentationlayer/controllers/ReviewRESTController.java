package com.marcotte.microservices.core.review.presentationlayer.controllers;



import com.marcotte.api.core.review.Review;
import com.marcotte.api.core.review.ReviewServiceAPI;
import com.marcotte.utils.exceptions.InvalidInputException;
import com.marcotte.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ReviewRESTController implements ReviewServiceAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ReviewRESTController.class);
    private final ServiceUtil serviceUtil;

    @Autowired
    public ReviewRESTController(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getReviews(int productId) {

        LOG.debug("/reviews MS returns the found product for productId: " + productId);

        if(productId < 1)throw new InvalidInputException("Invalid productId: " + productId);
        if(productId == 213)
        {
            LOG.debug("No reviews found for productId: {}", + productId);
            return new ArrayList<>();
        }

        List<Review> listReviews = new ArrayList<>();

        listReviews.add(new Review(productId, 2, "Author 1","uno" , "Content 1", serviceUtil.getServiceAddress()));
        listReviews.add(new Review(productId, 2, "Author 2", "dos", "Content 2", serviceUtil.getServiceAddress()));
        listReviews.add(new Review(productId, 3, "Author 3", "tres","Content 3", serviceUtil.getServiceAddress()));

        LOG.debug("/reviews found for response size: {}", listReviews.size());

        return listReviews;
    }
}
