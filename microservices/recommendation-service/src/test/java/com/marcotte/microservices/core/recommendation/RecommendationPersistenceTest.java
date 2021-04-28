package com.marcotte.microservices.core.recommendation;

import com.marcotte.microservices.core.recommendation.datalayer.RecommendationEntity;
import com.marcotte.microservices.core.recommendation.datalayer.RecommendationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@ExtendWith(SpringExtension.class)
@DataMongoTest
public class RecommendationPersistenceTest {

        @Autowired
        private RecommendationRepository repository;
        private RecommendationEntity savedEntity;

        @BeforeEach
        public void setupDb() {
            repository.deleteAll();

            RecommendationEntity entity = new RecommendationEntity(1, 2, "a", 3, "c");
            savedEntity = repository.save(entity);

            assertThat(savedEntity, samePropertyValuesAs(entity));
        }


        @Test
        public void createRecommendation() {

            RecommendationEntity newEntity = new RecommendationEntity(1, 3, "a", 3, "c");
            repository.save(newEntity);

            RecommendationEntity foundEntity = repository.findById(newEntity.getId()).get();

            assertThat(foundEntity, samePropertyValuesAs(newEntity));

            assertEquals(2, repository.count());
        }

        @Test
        public void updateRecommendation() {
            savedEntity.setAuthor("a2");
            repository.save(savedEntity);

            RecommendationEntity foundEntity = repository.findById(savedEntity.getId()).get();
            assertEquals(1, (long)foundEntity.getVersion());
            assertEquals("a2", foundEntity.getAuthor());
        }

        @Test
        public void deleteRecommendation() {
            repository.delete(savedEntity);
            assertFalse(repository.existsById(savedEntity.getId()));
        }

        @Test
        public void getRecommendationsByProductId() {
            List<RecommendationEntity> entityList = repository.findByProductId(savedEntity.getProductId());

            assertThat(entityList, hasSize(1));

            assertThat(entityList.get(0), samePropertyValuesAs(savedEntity));

        }

}
