package se.coolaganget.friendzip.miserycode;

import org.springframework.stereotype.Component;
import se.coolaganget.friendzip.model.User;

import java.util.HashMap;
import java.util.Map;

public class ProDataBase {

    private static Map<String, User> userProDatabase = new HashMap<>();
    private static Map<String, String> accessTokenProDatabase = new HashMap<>();


    public static Map<String, User> getUserProDatabase() {
        return userProDatabase;
    }

    public static Map<String, String> getAccessTokenProDatabase() {
        return accessTokenProDatabase;
    }

}
