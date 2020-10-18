package se.coolaganget.friendzip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FriendzipController {


    @GetMapping("/hello-world")
    @ResponseBody
    public String helloWorld() {
        return "Hello World";
    }
}
