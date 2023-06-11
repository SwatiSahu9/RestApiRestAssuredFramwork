package com.testautomation.apitesting.tests;
import com.testatomation.apitesting.listener.RestAssuredLisetner;
import com.testautomation.apitesting.utils.BaseTest;
import com.testautomation.apitesting.utils.FileNameConstant;
import io.qameta.allure.*;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

        @Epic("Epic-1")
        @Feature("Create update delete booking")
public class AllureReportGeneration extends BaseTest {
        private final static Logger logger = LogManager.getLogger(com.testautomation.apitesting.tests.EndToEndTest.class);
        @Story("Story 1")
        @Test(description="end to end Api testing")
        @Description("end to end testing")
        @Severity(SeverityLevel.CRITICAL)
        public void eneToendTest() {
            logger.info("e2eAPIRequest test execution started....");

            try {
                String postRequestBody = FileUtils.readFileToString(new File(FileNameConstant.POST_API_REQUEST_BODY), "UTF-8");

                Response postResponse = RestAssured
                        .given().filter(new AllureRestAssured()).filter(new RestAssuredLisetner()).baseUri("https://restful-booker.herokuapp.com/booking").body(postRequestBody).contentType(ContentType.JSON)
                        .when().post()
                        .then().assertThat().statusCode(200).extract().response();

                JsonPath jsonPath = postResponse.jsonPath();
                int bookingid = jsonPath.get("bookingid");

                //get request
                RestAssured
                        .given().filter(new AllureRestAssured()).filter(new RestAssuredLisetner()).baseUri("https://restful-booker.herokuapp.com/booking/").contentType(ContentType.JSON)
                        .when().get("{bookingid}", bookingid)
                        .then().assertThat().statusCode(200);

                //create Token
                String tokenRequestBody = FileUtils.readFileToString(new File(FileNameConstant.TOKEN_GENRATOR_API_FILE_PATH), "UTF-8");

                Response tokenResponce = RestAssured
                        .given().filter(new AllureRestAssured()).baseUri("https://restful-booker.herokuapp.com/auth").body(tokenRequestBody).contentType(ContentType.JSON)
                        .when().post()
                        .then().assertThat().statusCode(200).extract().response();

                JsonPath tokenJson = tokenResponce.jsonPath();
                String token = tokenJson.get("token");

                //update Api
                String patchApiRequestBody = FileUtils.readFileToString(new File(FileNameConstant.PATCH_API_REQUEST_FILE_PATH), "UTF-8");

                RestAssured
                        .given().baseUri("https://restful-booker.herokuapp.com/booking/").body(patchApiRequestBody).contentType(ContentType.JSON).header("Cookie", "token=" + token)
                        .when().patch("{bookingid}", bookingid)
                        .then().assertThat().statusCode(200).body("firstname", Matchers.equalTo("James"));

                //delete API
                RestAssured
                        .given().filter(new AllureRestAssured()).baseUri("https://restful-booker.herokuapp.com/booking/").contentType(ContentType.JSON).header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                        .when().delete("{bookingid}", bookingid)
                        .then().assertThat().statusCode(201).log().all();

            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("test Ended........");
        }

    }


