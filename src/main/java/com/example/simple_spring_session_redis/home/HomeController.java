package com.example.simple_spring_session_redis.home;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class HomeController {

    @GetMapping("/home")
    public String homeLogin() {
        return "home";
    }

    @GetMapping("/secure")
    public String securedPost(Principal principal) {
        return "Hello " + principal.getName() + ", secure endpoint!";
    }

    @PostMapping("/postCsrf")
    public String postCsrfTest(@RequestBody String text) {
        return "Post csrf end point: " + text;
    }

}
