package ru.yandex.praktikum;

import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.api.client.OrderApiClient;
import ru.yandex.praktikum.api.client.UserApiClient;
import ru.yandex.praktikum.api.helpers.GenerateData;
import ru.yandex.praktikum.api.helpers.GenerateOrder;
import ru.yandex.praktikum.api.pojo.order.OrderReqJson;
import ru.yandex.praktikum.api.pojo.user.UserReqJson;

import java.net.HttpURLConnection;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@Feature("Создание заказа")
public class CreateOrderTest {

    OrderReqJson orderReqJson;
    OrderApiClient orderApiClient;
    UserApiClient userApiClient;
    UserReqJson userReqJson;

    @Before
    public void setUp() {
        orderApiClient = new OrderApiClient();
        userApiClient = new UserApiClient();
        orderReqJson = GenerateOrder.createOrderJson();
        userReqJson = GenerateData.generateUserAccount();

    }

    @Test
    @DisplayName("Получение списка ингредиентов")
    public void getListIngredientsTest() {
        Response response = orderApiClient.getListIngredients();

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("data", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWitIngredientsTest() {
        userApiClient.createUser(userReqJson);
        userApiClient.clearToken();

        Response response = orderApiClient.createOrder(orderReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWitIngredientsAndAuthTest() {
        userApiClient.createUser(userReqJson);

        Response response = orderApiClient.createOrder(orderReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        orderReqJson.setIngredients(List.of());

        Response response = orderApiClient.createOrder(orderReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным айди ингредиентов")
    public void createOrderWithoutBadIdIngredientsTest() {
        orderReqJson.setIngredients(List.of("61c0c5a71d1f82001bdbaa6d"));

        Response response = orderApiClient.createOrder(orderReqJson);

        response.then()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("One or more ids provided are incorrect"));
    }

    @After
    public void tearDown(){
        GenerateData.deleteUserAccount(userReqJson);
    }

}
