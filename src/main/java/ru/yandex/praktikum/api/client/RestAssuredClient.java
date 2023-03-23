package ru.yandex.praktikum.api.client;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.with;

public class RestAssuredClient {

    protected static String BASE_URL = "https://stellarburgers.nomoreparties.site/api/";
    protected static String token;
    protected Filter req = new RequestLoggingFilter();
    protected Filter res = new ResponseLoggingFilter();
    protected RequestSpecification reqSpec = with()
            .filters(req, res)
            .filter(new AllureRestAssured())
            .baseUri(BASE_URL)
            .contentType(ContentType.JSON);

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        RestAssuredClient.token = token;
    }

    public static void setTokenToEmpty() {
        token = "";
    }
}
