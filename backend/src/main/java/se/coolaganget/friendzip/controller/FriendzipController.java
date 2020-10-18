package se.coolaganget.friendzip.controller;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import se.coolaganget.friendzip.config.OAuthProperties;

@Controller
public class FriendzipController {

    private final OAuthProperties oAuthProperties;


    FriendzipController(OAuthProperties oAuthProperties) {
        this.oAuthProperties = oAuthProperties;
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public String authenticate(String accessCode) throws IOException {
       return makePost("https://oauth2.googleapis.com/token", accessCode);
    }

    private String makePost(String url, String accessCode) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("code", accessCode));
        params.add(new BasicNameValuePair("client_id", oAuthProperties.getClientId()));
        params.add(new BasicNameValuePair("client_secret", oAuthProperties.getClientSecret()));
        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:3000/callback"));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = client.execute(httpPost);
        String jsonString = EntityUtils.toString(response.getEntity());
        client.close();

        return jsonString;

    }
}
