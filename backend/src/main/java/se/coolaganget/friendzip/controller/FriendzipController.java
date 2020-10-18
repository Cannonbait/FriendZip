package se.coolaganget.friendzip.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import se.coolaganget.friendzip.model.GoogleOAuthResponse;
import se.coolaganget.friendzip.model.User;

@Controller
public class FriendzipController {

    private final OAuthProperties oAuthProperties;


    FriendzipController(OAuthProperties oAuthProperties) {
        this.oAuthProperties = oAuthProperties;
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public String authenticate(String accessCode) throws IOException {
       String responseData =  getAccessToken("https://oauth2.googleapis.com/token", accessCode);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOAuthResponse googleOAuthResponse = objectMapper.readValue(responseData, GoogleOAuthResponse.class);

        User user = new User(googleOAuthResponse.getIdToken());
        String calendarURL = "https://www.googleapis.com/calendar/v3/calendars/" + user.getEmail() + "/events";

        System.out.println(calendarURL);
        retrieveEventList(calendarURL, googleOAuthResponse.getAccessToken());


       return responseData;
    }

    private String retrieveEventList(String url, String accessToken) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        url += "?timeMin=2020-10-18T10%3a00%3a00%2b0200";

        System.out.println(url);

        HttpGet httpGet = new HttpGet(url);

        List<NameValuePair> params = new ArrayList<>();
        httpGet.addHeader("Authorization", "Bearer " + accessToken);
        CloseableHttpResponse response = client.execute(httpGet);
        String jsonString = EntityUtils.toString(response.getEntity());
        System.out.println(jsonString);

        client.close();

        return jsonString;
    }

    private String getAccessToken(String url, String accessCode) throws IOException {

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
        System.out.println(jsonString);
        client.close();

        return jsonString;

    }
}
