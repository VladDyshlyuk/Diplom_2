package org.example.clients;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientClient {

    private static final String API_INGREDIENTS = "api/ingredients";

    public Response getIngredients(){
        return given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get(API_INGREDIENTS);
    }

}
