package com.marcotte.microservices.core.recommendation.businesslayer;

import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.microservices.core.recommendation.datalayer.RecommendationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {
    @Mapping(target = "serviceAddress",ignore = true)
    List<Recommendation> entityToModel(List<RecommendationEntity> entity);
    @Mappings({
            @Mapping(target = "id",ignore = true),
            @Mapping(target = "version",ignore = true)
    })
    List<RecommendationEntity> modelToEntity(List<Recommendation> model);
}