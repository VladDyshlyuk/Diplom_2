package org.example.generators;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.clients.IngredientClient;
import org.example.models.Ingredient;
import org.example.models.Order;

import java.util.ArrayList;
import java.util.List;

import static org.example.utils.Utils.randomNumber;

public class OrderGenerator {
    public static String[] getRandomIngredientList(Integer len){
        IngredientClient ingredientClient = new IngredientClient();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List<Object> list = ingredientClient.getIngredients().jsonPath().getList("data");
        ArrayList<String> ingredients = new ArrayList<String>();
        for (int i = 0; i < len; i++ ){
            String jsonString = gson.toJson(list.get(randomNumber(0, list.size() - 1)));
            ingredients.add(gson.fromJson(jsonString, Ingredient.class).get_id());
        }

        return ingredients.toArray(String[]::new);
    }

    public static Order randomOrder(){
        Order order = new Order();
        order.setIngredients(getRandomIngredientList(randomNumber(1, 5)));
        return order;
    }


}
