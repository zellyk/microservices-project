package com.marcotte.microservices.core.recommendation.businesslayer;


import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.microservices.core.recommendation.datalayer.RecommendationEntity;
import com.marcotte.microservices.core.recommendation.datalayer.RecommendationRepository;
import com.marcotte.utils.exceptions.InvalidInputException;
import com.marcotte.utils.http.ServiceUtil;
import com.mongodb.DuplicateKeyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationServiceImpl implements RecommendationService{


    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);

    private final RecommendationRepository repository;

    private final RecommendationMapper mapper;

    private final ServiceUtil serviceUtil;

    public RecommendationServiceImpl(RecommendationRepository repository, RecommendationMapper mapper, ServiceUtil serviceUtil) {
        this.repository = repository;
        this.mapper = mapper;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> findByProductId(int productId) {
        List<RecommendationEntity> entityList = repository.findByProductId(productId);
        List<Recommendation> list = mapper.entityListToModelList(entityList);
        list.forEach(e -> e.setServiceAddress(serviceUtil.getServiceAddress()));

        LOG.debug("Recommendations getByProductId: response size: {}", list.size());
        return list;
    }

    @Override
    public Recommendation createRecommendation(Recommendation model) {

        try{
            RecommendationEntity entity = mapper.modelToEntity(model);
            RecommendationEntity newEntity = repository.save(entity);

            LOG.debug("createRecommendation: entity created for productId: {}", model.getProductId());
            return mapper.entityToModel(newEntity);
        }catch (DuplicateKeyException dke){
            throw new InvalidInputException("Duplicate key, productId: " +model.getProductId());

        }

    }

    @Override
    public void deleteRecommendations(int productId) {

        LOG.debug("deleteRecommendation: delete entity with recommendationId: {} ", productId);
        repository.findByProductId(productId);



    }

}
