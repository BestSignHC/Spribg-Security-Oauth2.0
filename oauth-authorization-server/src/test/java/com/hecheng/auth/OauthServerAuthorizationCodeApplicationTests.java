package com.hecheng.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OauthServerAuthorizationCodeApplicationTests {

	@Test
	public void contextLoads() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String finalPassword = "{bcrypt}" + bCryptPasswordEncoder.encode("123");
		System.out.println(finalPassword);
	}

}
