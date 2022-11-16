package com.psrestassured;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.Map;

public class JavaPrintPeek {

    public static final String BASE_URL = "https://api.github.com";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";

    @Test
    public void peek()
    {
        RestAssured.get(BASE_URL).peek();
    }

    @Test
    public void prettyPeek()
    {
        RestAssured.get(BASE_URL).prettyPeek();
    }

    @Test
    public void print()
    {
        RestAssured.get(BASE_URL).print();
    }

    @Test
    public void prettyPrint()
    {
        RestAssured.get(BASE_URL).prettyPrint();
    }

}
