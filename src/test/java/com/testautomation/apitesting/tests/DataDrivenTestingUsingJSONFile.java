package com.testautomation.apitesting.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.testatomation.apitesting.listener.RestAssuredLisetner;
import com.testautomation.apitesting.utils.FileNameConstant;
import com.tests.automation.apitesting.pojos.Booking;
import com.tests.automation.apitesting.pojos.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.minidev.json.JSONArray;
import org.apache.commons.io.FileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class DataDrivenTestingUsingJSONFile {

    @Test(dataProvider = "getTestData")
    public void dataDrivenTestingUsingJson(LinkedHashMap<String,String>testData) throws JsonProcessingException {
        BookingDates bookingDates=new BookingDates("2023-03-25","2023-03-30");
        Booking booking=new Booking(testData.get("firstName"),testData.get("lastName"),"breakfast",1000,true,bookingDates);

        //serilaization
        ObjectMapper objectMapper=new ObjectMapper();
        String requestBody=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);

        Response response =RestAssured.given().filter(new RestAssuredLisetner()).baseUri("https://restful-booker.herokuapp.com/booking").body(requestBody).contentType(ContentType.JSON)
                .when().post()
                .then().assertThat().statusCode(200).log().all().extract().response();

    }

    @DataProvider(name = "getTestData")
public Object[] getTestDataUsingJson(){
        Object[]obj=null;
        try {
            String testData=FileUtils.readFileToString(new File(FileNameConstant.TEST_DATA_FILE_PATH),"UTF-8");
           JSONArray jsonArray= JsonPath.read(testData,"$");
           obj=new Object[jsonArray.size()];
           for(int i=0;i<jsonArray.size();i++){
             obj[i]=  jsonArray.get(i);
           }
        } catch (IOException e) {
            e.printStackTrace();
        }

      return obj;

}

}
