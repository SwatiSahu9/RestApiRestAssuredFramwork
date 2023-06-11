package com.testautomation.apitesting.tests;

import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstant;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;


import java.io.File;
import java.io.IOException;

public class PostAPIRequestUsingFile extends BaseTest {
    @Test
    public void postAPIRequest(){
        try {
            String postAPIRequestBody= FileUtils.readFileToString(new File(FileNameConstant.POST_API_REQUEST_BODY),"UTF-8");
        System.out.println(postAPIRequestBody);
        Response response= RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking").body(postAPIRequestBody).contentType(ContentType.JSON)
                  .when().post().
                  then().statusCode(200).log().all().body("booking.firstname",Matchers.equalTo("ShriRam")).extract().response();
            JSONArray jsonArray =JsonPath.read(response.body().asString(),"$.booking.bookingdates..checkin");
            System.out.println(jsonArray.get(0));

            int bookingId=JsonPath.read(response.body().asString(),"$.bookingid");
            RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking").contentType(ContentType.JSON)
                    .when().get("/{bookingId}",bookingId)
            .then().assertThat().statusCode(201);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
