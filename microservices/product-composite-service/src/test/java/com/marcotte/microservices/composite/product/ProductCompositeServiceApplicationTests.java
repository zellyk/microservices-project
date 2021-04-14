package com.marcotte.microservices.composite.product;

import com.marcotte.api.core.product.Product;
import com.marcotte.api.core.recommendation.Recommendation;
import com.marcotte.api.core.review.Review;
import com.marcotte.microservices.composite.product.integrationlayer.ProductCompositeIntegration;
import com.marcotte.utils.exceptions.InvalidInputException;
import com.marcotte.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static  org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
class ProductCompositeServiceApplicationTests {

	private static final int PRODUCT_ID_OKAY = 1;
	private static final int PRODUCT_ID_NOT_FOUND = 213;
	private static final String PRODUCT_ID_INVALID_STRING = "not-integer";
	private static final int PRODUCT_ID_INVALID_Negative_VALUE  = -1;

	@Autowired
	private WebTestClient client;

	@MockBean
	private ProductCompositeIntegration compositeIntegration;

	@BeforeEach
	void setup() {

		when(compositeIntegration.getProduct(PRODUCT_ID_OKAY))
				.thenReturn(new Product(PRODUCT_ID_OKAY, "name 1", 1, "mock address"));

		//BDD equivalent
		given(compositeIntegration.getProduct(PRODUCT_ID_OKAY))
				.willReturn(new Product(PRODUCT_ID_OKAY, "name 1", 1, "mock address"));


		when(compositeIntegration.getRecommendations(PRODUCT_ID_OKAY))
				.thenReturn(singletonList(new Recommendation(PRODUCT_ID_OKAY, 1, "author 1", 1, "test", "mock address")));

		when(compositeIntegration.getReviews(PRODUCT_ID_OKAY))
				.thenReturn(singletonList(new Review(PRODUCT_ID_OKAY, 1, "String","String", "string", "String" )));

		when(compositeIntegration.getProduct(PRODUCT_ID_NOT_FOUND))
				.thenThrow(new NotFoundException("NOT FOUND: " + PRODUCT_ID_NOT_FOUND));

		when(compositeIntegration.getProduct(PRODUCT_ID_INVALID_Negative_VALUE))
				.thenThrow(new InvalidInputException("INVALID: " + PRODUCT_ID_INVALID_Negative_VALUE));

	}


	@Test
	public void getProductById(){

		int expectedLength = 1;

		client.get()
				.uri("/product-composite/" + PRODUCT_ID_OKAY)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.productId").isEqualTo(PRODUCT_ID_OKAY)
				.jsonPath("$.recommendations.length()").isEqualTo(expectedLength)
				.jsonPath("$.reviews.length()").isEqualTo(expectedLength);

	}

	@Test
	public void getProductNotFound(){

		client.get()
				.uri("/product-composite/" + PRODUCT_ID_NOT_FOUND)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_NOT_FOUND)
				.jsonPath("$.message").isEqualTo("NOT FOUND: " + PRODUCT_ID_NOT_FOUND);

	}

	@Test
	public void getProductInvalidException(){

		client.get()
				.uri("/product-composite/" + PRODUCT_ID_INVALID_Negative_VALUE)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_INVALID_Negative_VALUE)
				.jsonPath("$.message").isEqualTo("INVALID: " + PRODUCT_ID_INVALID_Negative_VALUE);

	}

	@Test
	public void getProductInvalidParameterStringValue()
	{
		client.get()
				.uri("/product-composite/" + PRODUCT_ID_INVALID_STRING)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isBadRequest()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.path").isEqualTo("/product-composite/" + PRODUCT_ID_INVALID_STRING)
				.jsonPath("$.message").isEqualTo("Type mismatch.");

	}



	@Test
	void contextLoads() {
	}

}
