package org.example.clients;

import io.restassured.response.Response;
import org.example.models.User;
import org.example.models.UserCredentials;

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

    public Response delete(User user){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .delete(API_AUTH_USER);
    }

    public Response update(UserCredentials userCredentials){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userCredentials.getAccessToken() )
                .and()
                .body(userCredentials.getUser())
                .when()
                .patch(API_AUTH_USER);
    }



}
