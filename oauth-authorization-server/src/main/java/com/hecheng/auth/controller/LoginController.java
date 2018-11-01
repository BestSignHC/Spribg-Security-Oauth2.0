package com.hecheng.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @RequestMapping("/toLogin")
    public String login() {
        return "loginPage";
    }
}
