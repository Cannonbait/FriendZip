package se.coolaganget.friendzip.controller;

import config.OAuthProperties;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FriendzipController {

    private final Environment env;

    public FriendzipController(Environment env) {
        this.env = env;
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public String authenticate(String accessCode) throws IOException {
        System.out.println(accessCode);
       return makePost("https://oauth2.googleapis.com/token", accessCode);
    }

    private String makePost(String url, String accessCode) throws IOException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        System.out.println(env.getProperty("client-secret"));

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("code", accessCode));
        params.add(new BasicNameValuePair("client_id", System.getenv("CLIENT_SECRET")));
        params.add(new BasicNameValuePair("client_secret", System.getenv("CLIENT_ID")));
        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:3000/callback"));
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));

        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = client.execute(httpPost);
        String jsonString = EntityUtils.toString(response.getEntity());
        client.close();

        return jsonString;

    }
}
