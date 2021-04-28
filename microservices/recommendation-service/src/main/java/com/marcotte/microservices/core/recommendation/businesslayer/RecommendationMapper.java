package com.marcotte.microservices.core.recommendation.businesslayer;

import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.microservices.core.recommendation.datalayer.RecommendationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    @Mappings({
            @Mapping(target = "serviceAddress",ignore = true),
            @Mapping(target = "rate", source = "entity.rating")

    })
    Recommendation entityToModel(RecommendationEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "rating", source = "model.rate"),
            @Mapping(target = "version", ignore = true)
    })
    RecommendationEntity modelToEntity(Recommendation model);

    List<Recommendation> entityListToModelList(List<RecommendationEntity> entity);
    List<RecommendationEntity> modelListToEntityList(List<Recommendation> model);
}