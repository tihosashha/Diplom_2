package ru.yandex.praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.api.pojo.user.UserReqJson;

import java.net.HttpURLConnection;

public class UserApiClient extends RestAssuredClient {

    public static final String ENDPOINT_REGISTER = "/auth/register";
    public static final String ENDPOINT_LOGIN = "/auth/login";
    public static final String ENDPOINT_USER = "/auth/user";

    @Step("Отправка запроса на создание пользователя POST /api/auth/register | Почта = {body.email} Пароль = {body.password} Имя = {body.name}")
    public Response createUser(UserReqJson body) {
        Response response = reqSpec
                .body(body)
                .post(ENDPOINT_REGISTER);
        extractToken(response);
        return response;
    }

    @Step("Отправка запроса на авторизацию пользователя POST /api/auth/login | Почта = {body.email} Пароль = {body.password} Имя = {body.name}")
    public Response authorization(UserReqJson body) {
        Response response = reqSpec
                .body(body)
                .post(ENDPOINT_LOGIN);
        extractToken(response);
        return response;
    }

    @Step("Отправка запроса на редактирование пользователя PATCH /api/auth/user | Почта = {body.email} Пароль = {body.password} Имя = {body.name}")
    public Response changeDataUser(UserReqJson body) {
        return reqSpec
                .header("Authorization", RestAssuredClient.getToken())
                .body(body)
                .patch(ENDPOINT_USER);
    }

    @Step("Отправка запроса на удаление пользователя DELETE /api/auth/user")
    public void deleteUser() {
        reqSpec.header("Authorization", RestAssuredClient.getToken())
                .delete(ENDPOINT_USER);
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

    @Step("Удаление аккаунта, если он был создан")
    public static void deleteUserAccount(UserReqJson userReqJson) {
        UserApiClient userApiClient = new UserApiClient();
        Response responseAuth = userApiClient.authorization(userReqJson);
        if (responseAuth.statusCode() == HttpURLConnection.HTTP_OK) {
            userApiClient.deleteUser();
        } else {
            System.out.println("Пользователь создан не был");
        }
    }
}
