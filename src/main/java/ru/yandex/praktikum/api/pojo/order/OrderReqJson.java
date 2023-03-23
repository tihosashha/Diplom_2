package ru.yandex.praktikum.api.pojo.order;

import java.util.List;

public class OrderReqJson {
    private List<String> ingredients;

    public OrderReqJson() {
    }

    public OrderReqJson(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
