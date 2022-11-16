package com.psrestassured;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.hamcrest.Matchers;
import org.hamcrest.number.OrderingComparison;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.greaterThan;
import static org.testng.AssertJUnit.assertEquals;

public class BodyValidation {
    public static final String BASE_URL = "https://api.github.com/";

    @Test
    public void jsonPathReturnMaps()
    {
        Response responses = RestAssured.get(BASE_URL + "rate_limit");

        ResponseBody<?> body = responses.body();
        JsonPath jPath = body.jsonPath();

        JsonPath jPath2 = responses.body().jsonPath();

        Map<String, String> fullJson = jPath2.get();
        Map<String, String> subMap = jPath2.get("resources");
        Map<String, String> subMap2 = jPath2.get("resources.core");

        int value = jPath2.get("resources.core.limit");
        int value2 = jPath2.get("resources.graphql.remaining");

        //Get response body json items dynamically
        int limit = responses.getBody().jsonPath().get("resources.core.limit");
        int remaining = responses.getBody().jsonPath().get("resources.graphql.remaining");

        System.out.println(fullJson);
        System.out.println(subMap);
        System.out.println(subMap2);
        System.out.println(value);
        System.out.println(value2);
        System.out.println(limit);

        assertEquals(limit, 60);
        assertEquals(remaining, 0);
        assertEquals(value, 60);
        assertEquals(value2, 0);

        //ResponseAwareMatcher
        RestAssured.get(BASE_URL + "rate_limit")
                .then()
                .body("resources.core.limit", response -> Matchers.equalTo(response.body().jsonPath().get("resources.core.limit")))
                .body("resources.core.remaining", response -> Matchers.equalTo(Integer.parseInt(response.header("x-ratelimit-remaining"))));
    }

    @Test
    public void objectMappingUsingJackson()
    {
        User user = RestAssured.get(BASE_URL + "users/rest-assured").as(User.class, ObjectMapperType.JACKSON_2);
        assertEquals(user.getLogin(), "rest-assured");
        assertEquals(user.getId(), 19369327);
        assertEquals(user.getPublicRepos(), 2);
    }
}
