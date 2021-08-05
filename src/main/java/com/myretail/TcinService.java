package com.myretail;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class TcinService {

	private static String PRODUCTS_URL=
			"https://redsky.target.com/v3/pdp/tcin/%s?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics&key=candidate";

	public String getProductName(String id) throws JsonMappingException, JsonProcessingException {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> jsonResponse = restTemplate.getForEntity(getTcinUrl(id), String.class);

		//product.item.product_description.title
		JsonNode product = new ObjectMapper().readTree(jsonResponse.getBody());
		String title = 
				product.get("product").get("item").get("product_description").get("title").textValue();

		return title;
	}
	
	private String getTcinUrl(String id) {
		return String.format(PRODUCTS_URL, id);
	}
	
	
}
