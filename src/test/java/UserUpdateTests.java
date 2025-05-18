import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.clients.UserClient;
import org.example.models.User;
import org.example.models.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.example.generators.UserGenerator.randomUser;
import static org.example.utils.Utils.randomEmail;
import static org.example.utils.Utils.randomString;
import static org.junit.Assert.*;

public class UserUpdateTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static UserClient userClient;
    private static User user;
    private static UserCredentials userCredentials;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        userClient = new UserClient();
        user = randomUser();
        userClient.create(user);
        Response authResponse = userClient.auth(user);
        userCredentials = new UserCredentials();
        userCredentials.setUser(user);
        userCredentials.setAccessToken(authResponse.jsonPath().getString("accessToken"));
        userCredentials.setRefreshToken(authResponse.jsonPath().getString("refreshToken"));
    }

    @After
    public void tearDown(){
        userClient.delete(user);
    }

    @Test
    @Step("Можно отредактировать пользователя с авторизацией")
    public void editUserAuthorisedTest(){
        String newEmail = randomEmail();
        String newPassword = randomString(10);
        String newName = randomString(10);
        user.setEmail(newEmail);
        user.setName(newName);
        userCredentials.setUser(user);
        Response response = userClient.update(userCredentials);
        assertEquals(SC_OK, response.statusCode());
        assertEquals(newEmail, response.jsonPath().getString("user.email"));
        assertEquals(newName, response.jsonPath().getString("user.name"));
        assertTrue(response.jsonPath().getBoolean("success"));

    }



    @Test
    @Step("Нельзя отредактировать пользователя без авторизации")
    public void unableEditUserUnauthorised(){
        user = randomUser();
        userCredentials.setUser(user);
        userCredentials.setAccessToken("");
        Response response = userClient.update(userCredentials);
        assertEquals(SC_UNAUTHORIZED, response.statusCode());
        assertEquals("You should be authorised", response.jsonPath().getString("message"));
        assertFalse(response.jsonPath().getBoolean("success"));
    }


}
