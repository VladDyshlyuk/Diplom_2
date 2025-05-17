package org.example.generators;

import org.example.clients.UserClient;
import org.example.models.User;

import static org.example.utils.Utils.randomEmail;
import static org.example.utils.Utils.randomString;

public class UserGenerator {

    public static User randomUser(){
        User user = new User();
        user.setEmail(randomEmail());
        user.setName(randomString(10));
        user.setPassword(randomString(12));
        return user;
    }
}
