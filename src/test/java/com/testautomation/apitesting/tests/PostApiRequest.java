package com.testautomation.apitesting.tests;
import com.jayway.jsonpath.JsonPath;
import com.testautomation.apitesting.utils.BaseTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
@Test
public class PostApiRequest extends BaseTest {
    public void createBooking() {

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        //prepare request body
        JSONObject booking = new JSONObject();
        JSONObject bookingDate = new JSONObject();
booking.put("firstname","api testing");
booking.put("lastname","tutorial");
booking.put("totalprice",96756);
booking.put("depositpaid",true);
booking.put("additionalneeds","breakfast");
booking.put("bookingdates",bookingDate);
bookingDate.put("checkin","2023-03-25");
bookingDate.put("checkout","2023-03-30");

        Response response =RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com/booking")
                .contentType(ContentType.JSON)
                .body(booking.toString())
               // .log().all()
                .when()
                    .post()
                .then()
                .assertThat().statusCode(200)
                .body("booking.firstname", Matchers.equalTo("api testing"))
        .body("booking.lastname", Matchers.equalTo("tutorial"))
        .body("booking.bookingdates.checkin",Matchers.equalTo("2023-03-25"))
        .body("booking.bookingdates.checkout",Matchers.equalTo("2023-03-30")).extract().response();
         int bookingid=response.path("bookingid");

RestAssured
        .given()
        .contentType(ContentType.JSON)
        .pathParam("bookingid",bookingid)
        .baseUri("https://restful-booker.herokuapp.com/booking")
        .when()
        .get("{bookingid}")
        .then()
        .assertThat()
        .statusCode(200);
    }
}