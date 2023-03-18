package ru.yandex.praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.api.pojo.user.UserReqJson;

import java.net.HttpURLConnection;

public class UserApiClient extends RestAssuredClient {

    @Step("Отправка запроса на создание пользователя POST /api/auth/register | Почта = {body.email} Пароль = {body.password} Имя = {body.name}")
    public Response createUser(UserReqJson body) {
        Response response = reqSpec
                .body(body)
                .post("/auth/register");
        extractToken(response);
        return response;
    }

    @Step("Отправка запроса на авторизацию пользователя POST /api/auth/login | Почта = {body.email} Пароль = {body.password} Имя = {body.name}")
    public Response authorization(UserReqJson body) {
        Response response = reqSpec
                .body(body)
                .post("/auth/login");
        extractToken(response);
        return response;
    }

    @Step("Отправка запроса на редактирование пользователя PATCH /api/auth/user | Почта = {body.email} Пароль = {body.password} Имя = {body.name}")
    public Response changeDataUser(UserReqJson body) {
        return reqSpec
                .header("Authorization", RestAssuredClient.getToken())
                .body(body)
                .patch("/auth/user");
    }

    @Step("Отправка запроса на удаление пользователя DELETE /api/auth/user")
    public void deleteUser() {
        reqSpec.header("Authorization", RestAssuredClient.getToken())
                .delete("/auth/user");
    }

    public void clearToken() {
        RestAssuredClient.setTokenToEmpty();
    }

    @Step("Получение токена для авторизации")
    private void extractToken(Response response) {
        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            String token = response.then().extract().body().path("accessToken");
            RestAssuredClient.setToken(token);
        } else {
            clearToken();
        }
    }
}
