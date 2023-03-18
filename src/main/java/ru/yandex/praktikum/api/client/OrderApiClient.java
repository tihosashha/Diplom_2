package ru.yandex.praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.api.pojo.order.OrderReqJson;

public class OrderApiClient extends RestAssuredClient {

    @Step("Отправка запроса на получение ингредиентов GET /api/ingredients")
    public Response getListIngredients() {
        return reqSpec.get("/ingredients");
    }

    @Step("Отправка запроса на создание заказа POST /api/orders")
    public Response createOrder(OrderReqJson body) {
        return reqSpec
                .header("Authorization", token)
                .body(body)
                .post("/orders");
    }

    @Step("Отправка запроса на получение заказов GET /api/orders")
    public Response getOrders() {
        return reqSpec
                .header("Authorization", token)
                .get("/orders");
    }
}
