# myRetail
myRetail POC exercise

To run locally, grab the war in myRetail.zip and run with:
  java -jar myRetail.war

For testing purposes, the embedded db has data for ids 13860428, 54456119, 13264003, 12954218

Example requests:
GET localhost:8080/products/13860428

Response body:
{
    "id": "13860428",
    "name": "The Big Lebowski (Blu-ray)",
    "price": {
        "value": "15.00",
        "currency_code": "USD"
    }
}

PUT localhost:8080/products/13860428
with request body:
{
    "price": {
        "value": "10.00",
        "currency_code": "USD"
    }
}
