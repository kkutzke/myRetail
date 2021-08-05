package com.myretail;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myretail.database.PriceData;
import com.myretail.database.PriceRepository;

@Service
public class PriceService {

	@Autowired
	private PriceRepository repository;
	
	private static final String USD_CODE = "USD";
	
	@PostConstruct
	public void initForTesting() {
		repository.save(new PriceData("13860428", "15.00", USD_CODE));
		repository.save(new PriceData("54456119", "4.00", USD_CODE));
		repository.save(new PriceData("13264003", "4.25", USD_CODE));
		repository.save(new PriceData("12954218", "10.00", USD_CODE));
	}
	
	public Price getPrice(String id) {
		PriceData data = repository.findByProductId(id);
		if (data != null) {
			return new Price(data.getPrice(), data.getCurrencyCode());
		}
		return null;
	}
	
	public boolean updatePrice(String id, Price price) {
		PriceData data = repository.findByProductId(id);
		if (data != null) {
			String code = data.getCurrencyCode();		
			if (!StringUtils.isBlank(price.getCurrencyCode()))
			{
				code = price.getCurrencyCode();
			} 
			data.setPrice(price.getValue());
			data.setCurrencyCode(code);
			
			repository.save(data);
			return true;
		}
		return false;
	}
}
