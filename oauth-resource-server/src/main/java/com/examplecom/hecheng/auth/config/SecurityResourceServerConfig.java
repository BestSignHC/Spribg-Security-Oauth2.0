package com.examplecom.hecheng.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@EnableResourceServer
public class SecurityResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${app.oauth.jwt.publicKey}")
    private String jwtPublicKey;

    @Resource
    public DataSource dataSource;

    private static final String RESOURCE_ID = "demo_resource_server1";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/resource/**")
                .access("#oauth2.hasScope('query')");      // require 'query' scope to access /resource URL
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).tokenStore(tokenStore());
    }

//    JDBC方式
    @Bean
    public JdbcTokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    //jwt方式
//    @Bean
//    public TokenStore tokenStore() {
//        TokenStore tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
//        return tokenStore;
//    }

//    private JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
////        jwtAccessTokenConverter.setSigningKey("123");   //测试用
////        jwtAccessTokenConverter.setVerifier(new MacSigner("123"));  //必须这样设置，否则会没有SignerVerify
//        jwtAccessTokenConverter.setVerifier(new RsaVerifier(jwtPublicKey));
//
//        return jwtAccessTokenConverter;
//    }
}