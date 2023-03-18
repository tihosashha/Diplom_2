package ru.yandex.praktikum;

import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.api.client.UserApiClient;
import ru.yandex.praktikum.api.helpers.GenerateData;
import ru.yandex.praktikum.api.pojo.user.UserReqJson;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@Feature("Изменение данных пользователя")
public class ChangeUserDataTest {
    UserApiClient userApiClient;
    UserReqJson userReqJson;

    @Before
    public void setUp() {
        userApiClient = new UserApiClient();
        userReqJson = GenerateData.generateUserAccount();
        userApiClient.createUser(userReqJson);
        userApiClient.authorization(userReqJson);
    }

    @Test
    @DisplayName("Успешно изменение имени авторизованного пользователя")
    public void changeUserDataNameWithAuthTest() {
        userReqJson.setName(GenerateData.generateName());
        Response response = userApiClient.changeDataUser(userReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Успешно изменение почты авторизованного пользователя")
    public void changeUserDataEmailWithAuthTest() {
        userReqJson.setEmail(GenerateData.generateEmail());
        Response response = userApiClient.changeDataUser(userReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("user", notNullValue());
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя")
    public void changeUserDataNameWithoutAuthTest() {
        userReqJson.setName(GenerateData.generateName());
        userApiClient.clearToken();

        Response response = userApiClient.changeDataUser(userReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение почты неавторизованного пользователя")
    public void changeUserDataEmailWithoutAuthTest() {
        String emailBefore = userReqJson.getEmail();
        userReqJson.setEmail(GenerateData.generateEmail());
        userApiClient.clearToken();

        Response response = userApiClient.changeDataUser(userReqJson);
        userReqJson.setEmail(emailBefore);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void tearDown() {
        GenerateData.deleteUserAccount(userReqJson);
    }
}
