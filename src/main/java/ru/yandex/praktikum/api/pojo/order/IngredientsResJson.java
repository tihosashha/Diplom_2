package ru.yandex.praktikum.api.pojo.order;

import java.util.ArrayList;

public class IngredientsResJson {
    private boolean success;
    private ArrayList<DataJson> data;

    public IngredientsResJson() {
    }

    public IngredientsResJson(boolean success, ArrayList<DataJson> data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<DataJson> getData() {
        return data;
    }

    public void setData(ArrayList<DataJson> data) {
        this.data = data;
    }
}
