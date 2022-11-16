package com.psrestassured;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.hamcrest.Matchers;
import org.hamcrest.number.OrderingComparison;
import org.testng.annotations.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.greaterThan;
import static org.testng.AssertJUnit.assertEquals;

public class BasicTestJava {

    public static final String BASE_URL = "https://api.github.com";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";

    Map<String, String> expectedHeaders = Map.of("content-encoding", "gzip",
                                                "access-control-allow-origin", "*");

    @Test
    public void convenienceMethods()
    {
        Response response = RestAssured.get(BASE_URL);
        Headers headers = response.getHeaders();
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.getContentType(), CONTENT_TYPE);
    }

    @Test
    public void basicValidatableResponseExamples()
    {
        //Same as in method convenienceMethods
        RestAssured.get(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(CONTENT_TYPE)
                .header("x-ratelimit-limit", "60");

        //Syntactic sugar makes code more readable (same as line 72)
        RestAssured.get(BASE_URL)
                .then()
                .assertThat()
                    .statusCode(200)
                .and()
                    .contentType(CONTENT_TYPE)
                .and().assertThat()
                    .header("x-ratelimit-limit", "60");
    }

    @Test
    public void leveragingHamcrest()
    {
        RestAssured.get(BASE_URL)
                .then()
                .statusCode(200)
                .statusCode(Matchers.lessThan(300))
                .header("cache-control", Matchers.containsStringIgnoringCase("max-age=60"))
                .time(Matchers.lessThan(2L), TimeUnit.SECONDS)
                .header("etag", Matchers.notNullValue())
                .header("etag", Matchers.not(Matchers.emptyString()));
    }

    @Test
    public void complexHamcrestMatchers()
    {
        Clock cl = Clock.systemUTC();
        RestAssured.get(BASE_URL)
                .then()
                .header("x-ratelimit-limit", Integer::parseInt, Matchers.equalTo(60))
                .header("date", date -> LocalDate.parse(date, DateTimeFormatter.RFC_1123_DATE_TIME),
                        OrderingComparison.comparesEqualTo(LocalDate.now(cl)))
                .header("x-ratelimit-limit", response -> greaterThan(response.header("x-ratelimit-remaining")));
    }

    @Test
    public void usingMapsToTestHeaders()
    {
        RestAssured.get(BASE_URL)
                .then()
                .headers(expectedHeaders);
    }
}
