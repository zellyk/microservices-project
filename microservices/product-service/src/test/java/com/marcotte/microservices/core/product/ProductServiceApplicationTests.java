package com.marcotte.microservices.core.product;

import com.marcotte.api.core.product.Product;
import com.marcotte.microservices.core.product.datalayer.ProductEntity;
import com.marcotte.microservices.core.product.datalayer.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static reactor.core.publisher.Mono.just;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
class ProductServiceApplicationTests {

	private static final int PRODUCT_ID_OKAY = 1;
	private static final int PRODUCT_ID_NOT_FOUND = 13;
	private static final String PRODUCT_ID_INVALID_STRING = "not-integer";
	private static final int PRODUCT_ID_INVALID_Negative_VALUE  = -1;

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductRepository repository;

	@BeforeEach
	public void setupDb(){
		repository.deleteAll();
	}

	@Test
	public void getProductById(){

		ProductEntity entity = new ProductEntity(PRODUCT_ID_OKAY, "name-", 1);
		repository.save(entity);

		assertTrue(repository.findByProductId(PRODUCT_ID_OKAY).isPresent());

		client.get()
				.uri("/product/" + PRODUCT_ID_OKAY)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.productId").isEqualTo(PRODUCT_ID_OKAY);
	}



	@Test
	public void createProduct(){

		Product product = new Product(PRODUCT_ID_OKAY, "name-" + PRODUCT_ID_OKAY, 1, "sa" );

		client.post()
				.uri("/product")
				.body(just(product), Product.class)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.productId").isEqualTo(PRODUCT_ID_OKAY);

		assertTrue(repository.findByProductId(PRODUCT_ID_OKAY).isPresent());
	}


	@Test
	public void deleteProduct(){
		ProductEntity entity = new ProductEntity(PRODUCT_ID_OKAY, "name-", 1);
		repository.save(entity);

		assertTrue(repository.findByProductId(PRODUCT_ID_OKAY).isPresent());
		client.delete()
				.uri("/product/" + PRODUCT_ID_OKAY)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody();

		assertFalse(repository.findByProductId(PRODUCT_ID_OKAY).isPresent());
	}

	@Test
	public void getProductInvalidParameterString(){
		client.get()
				.uri("/product/" + PRODUCT_ID_INVALID_STRING)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product/" + PRODUCT_ID_INVALID_STRING)
				.jsonPath("$.message").isEqualTo("Type mismatch.");
	}

	@Test
	public void getProductNotFound(){
		client.get()
				.uri("/product/" + PRODUCT_ID_NOT_FOUND)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product/" + PRODUCT_ID_NOT_FOUND)
				.jsonPath("$.message").isEqualTo("No product found for productId: "+ PRODUCT_ID_NOT_FOUND);
	}


	@Test
	public void getProductInvalidParameterNegativeValue(){
		client.get()
				.uri("/product/" + PRODUCT_ID_INVALID_Negative_VALUE)
				.accept(APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product/" + PRODUCT_ID_INVALID_Negative_VALUE)
				.jsonPath("$.message").isEqualTo("Invalid productId: "+ PRODUCT_ID_INVALID_Negative_VALUE);
	}

	@Test
	void contextLoads() {
	}

}
