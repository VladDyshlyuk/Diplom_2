import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.clients.OrderClient;
import org.example.clients.UserClient;
import org.example.models.User;
import org.example.models.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.generators.UserGenerator.randomUser;
import static org.junit.Assert.*;

public class UserOrderRetrievalTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient;
    private static User user;
    private static UserCredentials userCredentials;
    private static OrderClient orderClient;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        userCredentials = new UserCredentials();
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = randomUser();
        userClient.create(user);
        userCredentials.setUser(user);
        userCredentials.setAccessToken(userClient.auth(user).jsonPath().getString("accessToken"));
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @Step("Получение заказов конкретного пользователя")
    public void authorizedOrderRetrievalTest(){
        Response response = orderClient.getUserOrders(userCredentials);
        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Нельзя получить заказ пользователя без авторизации")
    public void unauthorizedOrderRetrievalTest(){
        userCredentials.setAccessToken("");
        Response response = orderClient.getUserOrders(userCredentials);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
        assertEquals("You should be authorised", response.jsonPath().get("message"));
        assertFalse(response.jsonPath().getBoolean("success"));
    }

}
