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
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;

import se.coolaganget.friendzip.config.OAuthProperties;
import se.coolaganget.friendzip.miserycode.ProDataBase;
import se.coolaganget.friendzip.model.*;

@Controller
public class FriendzipController {

    private final OAuthProperties oAuthProperties;

    FriendzipController(OAuthProperties oAuthProperties) {
        this.oAuthProperties = oAuthProperties;
    }

    @PostMapping("/authenticate")
    @ResponseBody
    public String authenticate(String accessCode) throws IOException {
       String responseData = getAccessToken("https://oauth2.googleapis.com/token", accessCode);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOAuthResponse googleOAuthResponse = objectMapper.readValue(responseData, GoogleOAuthResponse.class);

        User user = new User(googleOAuthResponse.getIdToken());
        System.out.println(user.getEmail());

        ProDataBase.getUserProDatabase().put(user.getEmail(), user);
        ProDataBase.getAccessTokenProDatabase().put(user.getEmail(), googleOAuthResponse.getAccessToken());

       return responseData;
    }

    @PostMapping(value = "/zip", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public List<Slot> zipFriendsCalendar(@RequestBody ZipRequest zipRequest){

        System.out.println(ProDataBase.getUserProDatabase().size());
        ProDataBase.getUserProDatabase().keySet().forEach(item -> System.out.println(item));
        System.out.println("RequesterID: " + zipRequest.getRequesterId());

        User requestingUser = ProDataBase.getUserProDatabase().get(zipRequest.getRequesterId());
        User peerUser = ProDataBase.getUserProDatabase().get(zipRequest.getPeerId());

        String requesterEventsUrl = eventRequestUrlForUser(requestingUser);
        String peerEventsUrl = eventRequestUrlForUser(peerUser);

        try {
            String requesterEventsJson = retrieveEventList(requesterEventsUrl, ProDataBase.getAccessTokenProDatabase().get(requestingUser.getEmail()));
            String peerEventsJson = retrieveEventList(peerEventsUrl, ProDataBase.getAccessTokenProDatabase().get(peerUser.getEmail()));

            StupidCalendar requesterCalender = getUserStupidCalendar(requesterEventsJson);
            StupidCalendar peerCalendar = getUserStupidCalendar(peerEventsJson);

            return new Scheduler(Arrays.asList(Rule.NightRule())).schedule(Arrays.asList(requesterCalender, peerCalendar),
                    LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC),
                    LocalDateTime.ofInstant(Instant.now().plus(14, ChronoUnit.DAYS), ZoneOffset.UTC),
                    Duration.ofHours(2));

        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private StupidCalendar getUserStupidCalendar(String requesterEventsJson) {
        JSONObject requesterAcl = new JSONObject(requesterEventsJson);
        JSONArray itemArray = requesterAcl.getJSONArray("items");

        StupidCalendar calendar = new StupidCalendar();


            for(int i = 0; i < itemArray.length(); i++){
                try {
                    String startTime = itemArray.getJSONObject(i).getJSONObject("start").getString("dateTime");
                    String endTime = itemArray.getJSONObject(i).getJSONObject("end").getString("dateTime");

                    LocalDateTime startTimeDate = LocalDateTime.ofInstant(Instant.parse(startTime), ZoneOffset.UTC);
                    LocalDateTime endTimeDate = LocalDateTime.ofInstant(Instant.parse(endTime), ZoneOffset.UTC);

                    Duration eventDuration = Duration.between(startTimeDate, endTimeDate);
                    calendar.addNewBusyTime(new SlotImpl(startTimeDate, eventDuration));
                } catch (Exception e) {
                    System.out.println("Found a whole day event, get a fokkin life");
                }
            }

        return calendar;
    }

    private String eventRequestUrlForUser(User user){
        return "https://www.googleapis.com/calendar/v3/calendars/" + user.getEmail() + "/events";
    }

    private String retrieveEventList(String url, String accessToken) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();

        url += "?timeMin=2020-10-18T10%3a00%3a00%2b0200";

        System.out.println(url);

        HttpGet httpGet = new HttpGet(url);

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
