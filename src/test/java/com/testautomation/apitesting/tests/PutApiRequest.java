package com.testautomation.apitesting.tests;
import com.testautomation.apitesting.utils.FileNameConstant;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

public class PutApiRequest {
	@Test
	public void updateAPIRequest() {
		try {
			String postRequest = FileUtils.readFileToString(new File(FileNameConstant.POST_API_REQUEST_BODY), "UTF-8");
            String tokenRequestBody=FileUtils.readFileToString(new File(FileNameConstant.TOKEN_GENRATOR_API_FILE_PATH),"UTF-8");
			String updateAPIRequest=FileUtils.readFileToString(new File(FileNameConstant.PUT_API_REQUEST_FILE_PATH),"UTF-8");

			// post API Call:-

            Response response = RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking").body(postRequest).contentType(ContentType.JSON)
					.when().post()
					.then().assertThat().statusCode(200).extract().response();
			int bookingId1 = response.path("bookingid");
			System.out.println(bookingId1);
			JsonPath jsonPath = response.jsonPath();
			int bookingid2 = jsonPath.get("bookingid");
			System.out.println(bookingid2);

             //getAPI call:-

			RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking").contentType(ContentType.JSON)
					.when().get("/{bookingid}",bookingId1)
					.then().assertThat().statusCode(200);

			//get TokeGenration API Call

            Response response1=RestAssured.given().baseUri("https://restful-booker.herokuapp.com/auth").body(tokenRequestBody).contentType(ContentType.JSON).log().all().
                    when().post()
                    .then().assertThat().statusCode(200).extract().response();

			JsonPath jsonPath1=response1.jsonPath();
			String token=jsonPath1.get("token");
            System.out.println(token);

            //put API call

            Response response2=RestAssured.given().baseUri("https://restful-booker.herokuapp.com/booking").body(updateAPIRequest).contentType(ContentType.JSON).header("cookie","token="+token)
                    .when().put("/{bookingid}",bookingId1)
                    .then().assertThat().statusCode(200).body("firstname", Matchers.equalTo("James")).body("lastname",Matchers.equalTo("Brown")).extract().response();


		} catch (IOException e) {
			e.printStackTrace();
		}


	}
}