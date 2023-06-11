package com.testautomation.apitesting.tests;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetApiRequest {

    @Test
    public void getAllBooking(){
       Response response = RestAssured
                .given()
                 .contentType(ContentType.JSON)
                   .baseUri("https://restful-booker.herokuapp.com/booking")
        .when()
                 .get()
        .then()
                .assertThat()
                .statusLine("HTTP/1.1 200 OK")
                .extract().response();
         Assert.assertTrue(response.getBody().asString().contains("bookingid"));



    }

}
