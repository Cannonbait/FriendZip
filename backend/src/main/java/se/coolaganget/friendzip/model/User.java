package se.coolaganget.friendzip.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class User {

    DecodedJWT jwtToken;

    public User(String jwtToken) {
        this.jwtToken = JWT.decode(jwtToken);
    }

    public String getEmail() {
        return jwtToken.getClaim("email").asString();
    }

}
