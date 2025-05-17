package org.example.clients;

import io.restassured.response.Response;
import org.example.models.User;

import javax.swing.plaf.PanelUI;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String API_AUTH_REGISTER = "api/auth/register";
    private static final String API_PASSWORD_RESET = "api/password-reset";
    private static final String API_AUTH_TOKEN = "api/auth/token";
    private static final String API_AUTH_USER = "api/auth/user";
    private static final String API_AUTH_LOGIN = "api/auth/login";
    private static final String API_AUTH_LOGOUT = "api/auth/logout";

    public Response create(User user){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(API_AUTH_REGISTER);
    }


    public Response auth(User user){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(API_AUTH_LOGIN);
    }

    public Response getUserByAccessToken(String accessToken){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(accessToken)
                .when()
                .post(API_AUTH_REGISTER);
    }

    public Response logout(String refreshToken){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(refreshToken)
                .when()
                .post(API_AUTH_LOGOUT);
    }

    public Response delete(User user){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .delete(API_AUTH_USER);
    }

}
