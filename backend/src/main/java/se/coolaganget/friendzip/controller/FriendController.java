package se.coolaganget.friendzip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import se.coolaganget.friendzip.miserycode.ProDataBase;
import se.coolaganget.friendzip.model.Friend;
import se.coolaganget.friendzip.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FriendController {

    @GetMapping("/friends")
    @ResponseBody
    public List<Friend> getUsers() throws IOException {
        return ProDataBase.getUserProDatabase()
                .entrySet()
                .stream()
                .map(this::createFriend)
                .collect(Collectors.toList());
    }

    private Friend createFriend(Map.Entry<String, User> stringUserEntry) {
        return new Friend(stringUserEntry.getKey(), stringUserEntry.getKey());
    }


}
