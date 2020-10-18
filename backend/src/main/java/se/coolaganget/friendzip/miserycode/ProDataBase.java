package se.coolaganget.friendzip.miserycode;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import se.coolaganget.friendzip.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProDataBase {

    @Bean(name="user_database")
    public Map<String, User> UserProDatabase() {
        return new HashMap<>();
    }

    @Bean(name="token_database")
    public Map<String, String> accessTokenProDatabase() {
        return new HashMap<>();
    }

}
