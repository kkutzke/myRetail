package com.myretail.database;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PriceRepository extends MongoRepository<PriceData, String> {
	
	public PriceData findByProductId(String productId);
}
