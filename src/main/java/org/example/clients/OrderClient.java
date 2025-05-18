package org.example.clients;

import io.restassured.response.Response;
import org.example.models.Order;
import org.example.models.UserCredentials;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String API_ORDERS = "api/orders";
    private static final String API_ORDERS_ALL = "api/orders/all";
    private static final String API_INGREDIENTS = "api/ingredients";

    public Response getAllOrders(){
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(API_ORDERS_ALL);
    }

    public Response getUserOrders(UserCredentials userCredentials){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userCredentials.getAccessToken())
                .body(userCredentials.getUser())
                .and()
                .when()
                .get(API_ORDERS);
    }

    public Response getIngredients(){
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(API_INGREDIENTS);
    }

    public Response create(Order order, UserCredentials userCredentials){
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userCredentials.getAccessToken())
                .body(order)
                .and()
                .when()
                .post(API_ORDERS);
    }
}
