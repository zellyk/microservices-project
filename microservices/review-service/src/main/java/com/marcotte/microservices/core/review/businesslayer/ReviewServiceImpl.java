package com.marcotte.microservices.core.review.businesslayer;

import com.marcotte.api.core.review.Review;
import com.marcotte.microservices.core.review.datalayer.ReviewEntity;
import com.marcotte.microservices.core.review.datalayer.ReviewRepository;
import com.marcotte.utils.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ReviewRepository repository;

    private final ReviewMapper mapper;

    private final ServiceUtil serviceUtil;

    public ReviewServiceImpl(ReviewRepository repository, ReviewMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getProductById(int productId) {
        List<ReviewEntity> entityList = repository.findByProductId(productId);
        List<Review> list = mapper.entityListToModelList(entityList);
        list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

        LOG.debug("Review getByProductId: response size: {}", list.size());
        return list;
    }

    @Override
    public Review createReview(Review model) {
        ReviewEntity entity = mapper.modelToEntity(model);
        ReviewEntity newEntity = repository.save(entity);

        LOG.debug("ReviewService createReview: create a Review entity: {}/{}", model.getProductId(), model.getReviewId());
        return mapper.entityToModel(newEntity);

    }


    @Override
    public void deleteReviews(int productId) {

        LOG.debug("deleteReview: delete Review for productId: {} ", productId);
        repository.deleteAll(repository.findByProductId(productId));
    }
}