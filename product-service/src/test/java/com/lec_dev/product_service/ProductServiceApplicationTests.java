package com.lec_dev.product_service;

import java.math.BigDecimal;
import java.util.List;

import javax.print.attribute.standard.Media;

import com.lec_dev.product_service.repository.ProductRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;

import com.lec_dev.product_service.dto.ProductRequest;
import com.lec_dev.product_service.dto.ProductResponse;
import com.lec_dev.product_service.model.Product;

import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8.3.4");
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@SneakyThrows
    @Test
	void shouldCreateProduct( ) {
		ProductRequest productRequest = getProductRequest();
		String productRequeString = this.objectMapper.writeValueAsString(productRequest);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
		.contentType(org.springframework.http.MediaType.APPLICATION_JSON)
		.content(productRequeString))
		.andExpect(status().isCreated());

        Assertions.assertEquals(14, this.productRepository.findAll().size(), 1);
	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
		.name("Iphone 14")
		.description("Iphone 14")
		.price(BigDecimal.valueOf(1200))
		.build();
	}


	//Test de récupération
	@Test
	void shouldGetProduct(){
		try {
			this.mockMvc.perform(MockMvcRequestBuilders.get("/api/products"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()").value(12))
			.andExpect(jsonPath("$[1].name").value("Iphone 14"))
			.andExpect(jsonPath("$[1].description").value("Iphone 14"))
			.andExpect(jsonPath("$[1].price").value(1200));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
