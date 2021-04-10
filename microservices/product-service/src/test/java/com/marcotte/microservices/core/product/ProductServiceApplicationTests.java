package com.marcotte.microservices.core.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
class ProductServiceApplicationTests {

	private static final int PRODUCT_ID_OKAY = 1;
	private static final int PRODUCT_ID_NOT_FOUND = 13;
	private static final String PRODUCT_ID_INVALID_STRING = "not-integer";
	private static final int PRODUCT_ID_INVALID_Negative_VALUE  = -1;

	@Autowired
	private WebTestClient client;

	@Test
	public void getProductById(){
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
