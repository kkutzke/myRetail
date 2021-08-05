package com.myretail.database;

import org.springframework.data.annotation.Id;

public class PriceData {

	@Id
	public String id;
	
	public String productId;
	public String price;
	public String currencyCode;
	
	public PriceData(String productId, String price, String currencyCode) {
		this.productId = productId;
		this.price = price;
		this.currencyCode = currencyCode;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
}
