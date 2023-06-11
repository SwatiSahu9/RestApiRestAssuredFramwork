package com.testautomation.apitesting.tests;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testatomation.apitesting.listener.RestAssuredLisetner;
import com.testautomation.apitesting.utils.FileNameConstant;
import com.tests.automation.apitesting.pojos.Booking;
import com.tests.automation.apitesting.pojos.BookingDates;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DataDrivenTestingUsingExcelFile {
    @Test(dataProvider = "ExcelTestData")
    public void dataDrivenTestingUsingExcel(Map<String,String>testData){
        System.out.println(testData.get("FirstName"));
        int totalprice=Integer.parseInt(testData.get("TotalPrice"));
        BookingDates bookingDates=new BookingDates("2023-03-25","2023-03-30");
        Booking booking=new Booking(testData.get("FirstName"),testData.get("LastName"),"breakfast",totalprice,true,bookingDates);

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

    @DataProvider(name="ExcelTestData")
    public Object [][]getTestData(){
    String query ="select * from sheet1 where run='yes'";

    Object[][] objArray=null;
    Map<String,String> testData=null;
    List<Map<String,String>> testDataList=null;

    Fillo fillo=new Fillo();
    Connection connection=null;
    Recordset recordset=null;

        try {
            connection= fillo.getConnection(FileNameConstant.TEST_DATA_EXCEL_FILE_PATH);
            recordset=connection.executeQuery(query);
            testDataList=new ArrayList<Map<String,String>>();
            while (recordset.next()){
               testData=new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
               for(String field:recordset.getFieldNames()){
                   testData.put(field,recordset.getField(field));
               }
               testDataList.add(testData);
            }
            objArray=new Object[testDataList.size()][1];
           for(int i=0;i<testDataList.size();i++){

               objArray[i][0]=testDataList.get(i);
           }
        } catch (FilloException e) {
            e.printStackTrace();
        }
        return objArray;
    }
}
