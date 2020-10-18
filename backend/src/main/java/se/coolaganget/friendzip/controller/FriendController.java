package se.coolaganget.friendzip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import se.coolaganget.friendzip.model.Friend;

import java.io.IOException;
import java.util.List;

@Controller
public class FriendController {

    @GetMapping("/friends")
    @ResponseBody
    public List<Friend> getUsers() throws IOException {
        Friend user1 = new Friend("a", "b");
        Friend user2 = new Friend("c", "d");
        return List.of(user1, user2);
    }
}
