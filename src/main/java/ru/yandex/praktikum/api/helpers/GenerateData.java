package ru.yandex.praktikum.api.helpers;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.api.client.UserApiClient;
import ru.yandex.praktikum.api.pojo.user.UserReqJson;

import java.net.HttpURLConnection;

public class GenerateData {
    private static String email;
    private static String password;
    private static String name;
    private static final Faker faker = new Faker();

    private static void createUserData() {
        generateEmail();
        generatePassword();
        generateName();
    }

    @Step("Создание JSON пользователя Почта, Пароль, Имя")
    public static UserReqJson generateUserAccount() {
        createUserData();
        return new UserReqJson(email, password, name);
    }

    public static String generateEmail() {
        return email = faker.internet().emailAddress();
    }

    public static String generatePassword() {
        return password = faker.internet().password();
    }

    public static String generateName() {
        return name = faker.name().username();
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
