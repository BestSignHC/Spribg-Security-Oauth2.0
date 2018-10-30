package com.examplecom.hecheng.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ResourceController {

    @GetMapping("/resource/query")
    public String queryAction(Principal principal) {
        return principal.getName() + "query at " + System.currentTimeMillis();
    }
}
