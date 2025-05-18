import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.clients.OrderClient;
import org.example.clients.UserClient;
import org.example.models.Order;
import org.example.models.User;
import org.example.models.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.generators.OrderGenerator.randomOrder;
import static org.example.generators.UserGenerator.randomUser;
import static org.example.utils.Utils.randomString;
import static org.junit.Assert.*;

public class OrderCreationTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient;
    private static User user;
    private static Order order;
    private static OrderClient orderClient;
    private static UserCredentials userCredentials;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        order = new Order();
        userCredentials = new UserCredentials();
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = randomUser();
        userClient.create(user);
        userCredentials.setUser(user);
        userCredentials.setAccessToken(userClient.auth(user).jsonPath().getString("accessToken"));
        order = randomOrder();
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void authorizedOrderCreationTest(){
        Response response = orderClient.create(order, userCredentials);
        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void unauthorizedOrderCreationTest(){
        userCredentials.setAccessToken("");
        Response response = orderClient.create(order, userCredentials);
        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами")
    public void ingredientsOrderCreationTest(){
        Response response = orderClient.create(order, userCredentials);
        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Нельзя создать заказ без ингредиентов")
    public void noIngredientsOrderCreationTest(){
        order.setIngredients(null);
        Response response = orderClient.create(order, userCredentials);
        assertEquals(SC_BAD_REQUEST, response.statusCode());
        assertEquals("Ingredient ids must be provided", response.jsonPath().get("message"));
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @Test
    @DisplayName("Нельзя создать заказ c неверным хэшем ингредиентов")
    public void wrongHashOrderCreationTest(){
        String[] wrongIngredients = new String[1];
        wrongIngredients[0] = randomString(10);
        order.setIngredients(wrongIngredients);
        Response response = orderClient.create(order, userCredentials);
        assertEquals(SC_INTERNAL_SERVER_ERROR, response.statusCode());
    }










}
