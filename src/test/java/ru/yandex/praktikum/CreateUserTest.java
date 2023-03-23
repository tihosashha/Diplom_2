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

@Feature("Создание пользователя")
public class CreateUserTest {

    UserReqJson userReqJson;
    UserApiClient userApiClient;

    @Before
    public void setUp() {
        userApiClient = new UserApiClient();
        userReqJson = GenerateData.generateUserAccount();

    }

    @Test
    @DisplayName("Успешное создание уникального пользователя")
    public void createSuccessUserTest() {
        Response response = userApiClient.createUser(userReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Повторно создание пользователя, который уже зарегистрирован")
    public void createUserWhoAlreadyExistTest(){
        userApiClient.createUser(userReqJson);

        Response response = userApiClient.createUser(userReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("success",equalTo(false))
                .body("message",equalTo("User already exists"));

    }

    @Test
    @DisplayName("Создание пользователя без почты")
    public void createUserWithoutEmailTest(){
        userReqJson.setEmail(null);

        Response response = userApiClient.createUser(userReqJson);

        response.then()
                .assertThat()
                .statusCode(403)
                .body("success",equalTo(false))
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    public void createUserWithoutNameTest(){
        userReqJson.setName(null);

        Response response = userApiClient.createUser(userReqJson);

        response.then()
                .assertThat()
                .statusCode(403)
                .body("success",equalTo(false))
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    public void createUserWithoutPasswordTest(){
        userReqJson.setPassword(null);

        Response response = userApiClient.createUser(userReqJson);

        response.then()
                .assertThat()
                .statusCode(403)
                .body("success",equalTo(false))
                .body("message",equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown(){
        UserApiClient.deleteUserAccount(userReqJson);
    }
}
