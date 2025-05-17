import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.clients.UserClient;
import org.example.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.generators.UserGenerator.randomUser;
import static org.example.utils.Utils.randomString;
import static org.junit.Assert.*;

public class UserLoginTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient;
    private static User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        userClient = new UserClient();
        user = randomUser();
        userClient.create(user);
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @Step("Можно залогиниться под существующим пользователем")
    public void existingUserAuthTest(){
        Response response = userClient.auth(user);
        assertEquals(SC_OK, response.statusCode());
        assertTrue(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Нельзя залогиниться с неверным логином")
    public void unableToAuthWrongLogin(){
        user.setEmail(randomString());
        Response response = userClient.auth(user);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Нельзя залогиниться с неверным паролем")
    public void unableToAuthWrongPassword(){
        user.setPassword(randomString());
        Response response = userClient.auth(user);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
        assertFalse(response.jsonPath().getBoolean("success"));
    }
}
