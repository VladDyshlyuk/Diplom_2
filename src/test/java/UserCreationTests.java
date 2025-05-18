import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.example.clients.UserClient;
import org.example.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.example.generators.UserGenerator.randomUser;
import static org.junit.Assert.*;

import io.restassured.response.Response;

public class UserCreationTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient;
    private static User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        userClient = new UserClient();
        user = randomUser();
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @Step("Уникального пользователя можно создать")
    public void uniqueUserCreationTest(){
        Response response = userClient.create(user);
        assertEquals(SC_OK, response.statusCode());
    }

    @Test
    @Step("Нельзя создать существующего пользователя")
    public void errorReturnedUserExistsTest(){
        userClient.create(user);
        Response response = userClient.create(user);
        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals("User already exists", response.jsonPath().get("message"));
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Нельзя создать пользователя без имени")
    public void errorReturnedNoNameTest(){
        User user = randomUser();
        user.setName(null);
        Response response = userClient.create(user);
        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals("Email, password and name are required fields", response.jsonPath().get("message"));
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Нельзя создать пользователя без почты")
    public void errorReturnedNoEmailTest(){
        User user = randomUser();
        user.setEmail(null);
        Response response = userClient.create(user);
        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals("Email, password and name are required fields", response.jsonPath().get("message"));
        assertFalse(response.jsonPath().getBoolean("success"));
    }

    @Test
    @Step("Нельзя создать пользователя без пароля")
    public void errorReturnedNoPasswordTest(){
        User user = randomUser();
        user.setPassword(null);
        Response response = userClient.create(user);
        assertEquals(SC_FORBIDDEN, response.statusCode());
        assertEquals("Email, password and name are required fields", response.jsonPath().get("message"));
        assertFalse(response.jsonPath().getBoolean("success"));
    }

}
