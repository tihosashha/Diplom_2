package ru.yandex.praktikum;

import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.api.client.OrderApiClient;
import ru.yandex.praktikum.api.client.UserApiClient;
import ru.yandex.praktikum.api.helpers.GenerateData;
import ru.yandex.praktikum.api.pojo.user.UserReqJson;

import java.lang.reflect.Array;
import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyArray;

@Feature("Получение заказов конкретного пользователя")
public class GetUserOrderTest {
    OrderApiClient orderApiClient;
    UserApiClient userApiClient;
    UserReqJson userReqJson;

    @Before
    public void setUp() {
        orderApiClient = new OrderApiClient();
        userApiClient = new UserApiClient();
        userReqJson = GenerateData.generateUserAccount();
        userApiClient.createUser(userReqJson);

    }

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getAnOrderFromAnAuthorizedUserTest() {
        Response response = orderApiClient.getOrders();

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("orders", notNullValue() )
                .body("total", notNullValue())
                .body("totalToday", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getAnOrderWithoutAuthorizedUserTest() {
        userApiClient.clearToken();

        Response response = orderApiClient.getOrders();

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
