package com.myretail;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import com.myretail.ApiController;
import com.myretail.TcinService;

@ExtendWith(MockitoExtension.class)
public class ApiControllerTest {
	
	private static final String TEST_JSON = "{\"id\":\"13860428\",\"name\":\"The Big Lebowski (Blu-ray)\",\"price\":{\"value\":\"15.00\",\"currency_code\":\"USD\"}}";
			
	@Mock
	TcinService tcinService;
	
	@Mock
	PriceService priceService;
	
	@InjectMocks
	ApiController apiController;
		
	@Test
	public void testGetProductJson() throws Exception
	{
		when(tcinService.getProductName(any())).thenReturn("The Big Lebowski (Blu-ray)");
		when(priceService.getPrice(any())).thenReturn(new Price("15.00", "USD"));
		
		String json = apiController.getProductJson("13860428");
		assertEquals(TEST_JSON, json);
	}
	
	@Test
	public void testGet_badRequest()
	{
		Exception e = assertThrows(ResponseStatusException.class, () ->
			apiController.getProductJson("asdf"));
		assertEquals(e.getMessage(), HttpStatus.BAD_REQUEST.toString());
	}
	
	@Test
	public void testGet_notFound_tcin() throws Exception
	{
		when(tcinService.getProductName(any())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
		
		Exception e = assertThrows(ResponseStatusException.class, () ->
			apiController.getProductJson("12345"));
		assertEquals(e.getMessage(), HttpStatus.NOT_FOUND.toString());
	}
	
	@Test
	public void testGet_notFound_price() throws Exception
	{
		when(tcinService.getProductName(any())).thenReturn("The Big Lebowski (Blu-ray)");
		when(priceService.getPrice(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
		
		Exception e = assertThrows(ResponseStatusException.class, () ->
			apiController.getProductJson("12345"));
		assertEquals(e.getMessage(), HttpStatus.NOT_FOUND.toString());
	}
	
	@Test
	public void testUpdatePrice() throws Exception
	{
		when(priceService.updatePrice(any(), any())).thenReturn(true);
		
		Product request = new Product();
		request.setPrice(new Price("10.00", "USD"));
		apiController.updatePrice("13860428", request);
		
		verify(priceService).updatePrice(eq("13860428"),  any());
	}
	
	@Test
	public void testUpdatePrice_notUpdated() throws Exception
	{
		when(priceService.updatePrice(any(), any())).thenReturn(false);
		
		Product request = new Product();
		request.setPrice(new Price("10.00", "USD"));
		
		Exception e = assertThrows(ResponseStatusException.class, () ->
			apiController.updatePrice("12345", request));
		assertEquals(e.getMessage(), HttpStatus.NOT_FOUND.toString());
	}
	
	@Test
	public void testUpdatePrice_badRequest() throws Exception
	{	
		Product request = new Product();
		request.setPrice(new Price("10.00a", "USD"));
		
		Exception e = assertThrows(ResponseStatusException.class, () ->
			apiController.updatePrice("12345", request));
		assertEquals(e.getMessage(), HttpStatus.BAD_REQUEST.toString());
	}
	
}
