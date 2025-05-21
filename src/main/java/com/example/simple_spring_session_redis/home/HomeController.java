package com.example.simple_spring_session_redis.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/home")
    public String homeLogin() {
        return "home";
    }

    @GetMapping("/secure")
    @ResponseBody
    public String securedPost(Principal principal) {
        return "Hello " + principal.getName() + ", secure endpoint!";
    }
}
