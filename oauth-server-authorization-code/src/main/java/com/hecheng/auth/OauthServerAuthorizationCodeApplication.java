package com.hecheng.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class OauthServerAuthorizationCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(OauthServerAuthorizationCodeApplication.class, args);
	}
}
