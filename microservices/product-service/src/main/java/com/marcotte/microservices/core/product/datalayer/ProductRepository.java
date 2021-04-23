package com.marcotte.microservices.core.product.datalayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, String> {


    Optional<ProductEntity> findByProductId(int productId);

}
