package com.testautomation.apitesting.tests;
import com.testautomation.apitesting.utils.FileNameConstant;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;


import java.io.File;
import java.io.IOException;

public class PatchAPIRequest {
    @Test
    public void partialUpdatePatchRequest(){
        try {
            String postRequestBody=FileUtils.readFileToString(new File(FileNameConstant.POST_API_REQUEST_BODY),"UTF-8");
            String patchRequestBody=FileUtils.readFileToString(new File(FileNameConstant.PATCH_API_REQUEST_FILE_PATH),"UTF-8");
            String tokenRequestBody=FileUtils.readFileToString(new File(FileNameConstant.TOKEN_GENRATOR_API_FILE_PATH),"UTF-8");
            //post call
           Response response = RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking").body(postRequestBody).contentType(ContentType.JSON)
                    .when().post()
                    .then().assertThat().statusCode(200).extract().response();

           JsonPath jsonPath = response.jsonPath();
          int bookingid= jsonPath.get("bookingid");

           //get call
            Response response1=RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking/").contentType(ContentType.JSON)
                    .when().get("{bookingid}",bookingid)
                    .then().assertThat().statusCode(200).extract().response();

            //create Token
           Response response2= RestAssured.given().baseUri("https://restful-booker.herokuapp.com/auth").body(tokenRequestBody).contentType(ContentType.JSON)
                   .log().all() .when().post()
                    .then().assertThat().statusCode(200).log().all().extract().response();
            JsonPath jsonPath1=response2.jsonPath();
            String token=jsonPath1.get("token");
            System.out.println(token);

           //patch call
            RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking").body(patchRequestBody).contentType(ContentType.JSON).header("Cookie","token="+token).log().all()
                    .when().patch("/{bookingid}",bookingid)
                    .then().assertThat().statusCode(200).log().all();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }



}
