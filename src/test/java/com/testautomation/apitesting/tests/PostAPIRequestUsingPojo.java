package com.testautomation.apitesting.tests;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.testautomation.apitesting.utils.FileNameConstant;
import com.tests.automation.apitesting.pojos.Booking;
import com.tests.automation.apitesting.pojos.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class PostAPIRequestUsingPojo {
    @Test
    public void postAPIRequest()  {

                               // jsonSchemaValidator
        try {
                String jsonSchema= FileUtils.readFileToString(new File(FileNameConstant.JSON_SCHEMA_FILE_PATH),"UTF-8");

            //Serialization using Jackson
            BookingDates bookingDates=new BookingDates("2018-01-01","2019-01-01");
            Booking booking=new Booking("Shree","RamHanuman","BreakFast",1000,true ,bookingDates);
            ObjectMapper objectMapper=new ObjectMapper();
                String requestBody=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
                System.out.println(requestBody);


           //deSerialization Using Jackson
            Booking bookingDetails=objectMapper.readValue(requestBody,Booking.class);
            System.out.println(bookingDetails.getFirstname());
            System.out.println(bookingDetails.getLastname());
            System.out.println(bookingDetails.getTotalprice());
            System.out.println(bookingDetails.getBookingdates().getCheckin());
            System.out.println(bookingDetails.getBookingdates().getCheckout());

           Response response = RestAssured
                    .given().baseUri("https://restful-booker.herokuapp.com/booking").contentType(ContentType.JSON).body(requestBody)
                    .when().post()
                    .then().assertThat().statusCode(200).extract().response();

           int bookinid=response.path("bookingid");
            System.out.println(jsonSchema);
          RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking").contentType(ContentType.JSON)
                   .when().get("/{bookinid}",bookinid)
                  .then().statusCode(200).body(JsonSchemaValidator.matchesJsonSchema(jsonSchema));

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
