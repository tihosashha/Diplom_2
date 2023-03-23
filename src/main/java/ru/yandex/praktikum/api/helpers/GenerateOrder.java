package ru.yandex.praktikum.api.helpers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.api.client.OrderApiClient;
import ru.yandex.praktikum.api.pojo.order.DataJson;
import ru.yandex.praktikum.api.pojo.order.IngredientsResJson;
import ru.yandex.praktikum.api.pojo.order.OrderReqJson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateOrder {
    private static final OrderApiClient orderApiClient = new OrderApiClient();
    private static final List<String> listOfIngredients = new ArrayList<>();

    private static void getIdIngredients() {
        Response response = orderApiClient.getListIngredients();
        IngredientsResJson ingredientsResJson = response.then().extract().as(IngredientsResJson.class);
        ArrayList<DataJson> dataJson = ingredientsResJson.getData();
        Random r = new Random();
        List<DataJson> rndData = dataJson.subList(r.nextInt(dataJson.size()), dataJson.size());
        rndData.forEach((x) -> listOfIngredients.add(x.get_id()));
    }

    @Step("Создание JSON заказа Ингредиенты")
    public static OrderReqJson createOrderJson() {
        getIdIngredients();
        return new OrderReqJson(listOfIngredients);
    }
}
