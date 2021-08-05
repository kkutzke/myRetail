package com.myretail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiController {
	
	@Autowired
	private PriceService priceService;
	
	@Autowired
	private TcinService tcinService;
	
	@GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getProductJson(@PathVariable String id) {
		
		try {
			Integer.parseInt(id);
			
			Product product = new Product();
			product.setId(id);
			product.setName(tcinService.getProductName(id));
			Price price = priceService.getPrice(id);
			
			if (price == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			product.setPrice(price);
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			return objectMapper.writeValueAsString(product);
		}
		catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode());
		}
		catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/products/{id}")
	public void updatePrice(@PathVariable String id, @RequestBody Product product) {
		try {
			validatePutRequest(id, product);
			boolean updated = priceService.updatePrice(id, product.getPrice());
			
			if (!updated) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		catch (NumberFormatException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	public void validatePutRequest(String id, Product product) {
		Integer.parseInt(id);	
		if (product.getPrice() == null || product.getPrice().getValue() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		Double.valueOf(product.getPrice().getValue());
	}
}
