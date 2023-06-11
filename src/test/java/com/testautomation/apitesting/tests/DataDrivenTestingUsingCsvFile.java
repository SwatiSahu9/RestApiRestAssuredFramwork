package com.testautomation.apitesting.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.testatomation.apitesting.listener.RestAssuredLisetner;
import com.testautomation.apitesting.utils.FileNameConstant;
import com.tests.automation.apitesting.pojos.Booking;
import com.tests.automation.apitesting.pojos.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataDrivenTestingUsingCsvFile {
    @Test(dataProvider = "CsvTestData")
    public void DataDrivenTesting(Map<String,String>testData){
        int totalprice=Integer.parseInt(testData.get("totalprice"));
        BookingDates bookingDates=new BookingDates("2023-03-25","2023-03-30");
        Booking booking=new Booking(testData.get("firstName"),testData.get("lastName"),"breakfast",totalprice,true,bookingDates);

        //serilaization
        ObjectMapper objectMapper=new ObjectMapper();
        String requestBody= null;
        try {
            requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(booking);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Response response = RestAssured.given().filter(new RestAssuredLisetner()).baseUri("https://restful-booker.herokuapp.com/booking").body(requestBody).contentType(ContentType.JSON)
                .when().post()
                .then().assertThat().statusCode(200).log().all().extract().response();

    }



   @DataProvider(name="CsvTestData")
    public Object [][] getTestData(){
       Object[][]objArray=null;
       Map<String,String> map=null;
       List<Map<String,String>>testDataList =null;

       try {
           CSVReader csvReader = new CSVReader(new FileReader(FileNameConstant.TEST_DATA_CSV_FILE_PATH));
           testDataList = new ArrayList<Map<String, String>>();
           String[] line =null;
           int count=0;


           while ((line = csvReader.readNext()) != null) {
               if(count==0){
                   count++;
                   continue;
               }
          map=new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
          map.put("firstname",line[0]);
          map.put("lastname",line[1]);
          map.put("totalprice",line[2]);
          testDataList.add(map);

           }

           objArray=new Object[testDataList.size()][1];
           for(int i=0;i<testDataList.size();i++){
           objArray[i][0]=testDataList.get(i);
}


       } catch (FileNotFoundException e) {
           e.printStackTrace();
       } catch (IOException e) {
           e.printStackTrace();
       } catch (CsvValidationException e) {
           e.printStackTrace();
         }



       return objArray;

    }}