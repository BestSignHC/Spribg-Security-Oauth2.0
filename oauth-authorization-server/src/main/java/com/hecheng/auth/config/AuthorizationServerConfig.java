package com.hecheng.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${app.oauth.jwt.privateKey}")
    private String jwtPrivateKey;

    @Value("${app.oauth.jwt.publicKey}")
    private String jwtPublicKey;

    @Resource
    private DataSource dataSource;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许表单认证，用于post方式请求code
        //当然必须带上：
        //Content-Type: application/x-www-form-urlencoded;charset=utf-8
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        //*************** 使用内存方式 BEGIN*********************//
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        String finalPassword = "{bcrypt}" + bCryptPasswordEncoder.encode("123");
//        clients.inMemory()
//                .withClient("client1")      //设置client_id
//                .authorizedGrantTypes("authorization_code", "refresh_token") //设置允许authorization_code以及refresh_token授权方式
//                .scopes("query")                    //设置权限列表
//                .secret(finalPassword)
//                .redirectUris("http://www.baidu.com");
        //*************** 使用内存方式 END *********************//

        //*************** 使用JDBC方式 BEGIN*********************//
//        clients.jdbc(dataSource);
        clients.withClientDetails(new JdbcClientDetailsService(dataSource));
        //*************** 使用JDBC方式 END *********************//
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //允许使用GET/POST方式请求TOKEN
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        endpoints.authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource));

        endpoints.authenticationManager(authenticationManager);

        //这个不配的话refresh_token会报错
        endpoints.userDetailsService(new JdbcUserDetailsManager(dataSource));

        //*************** 使用JDBC方式 BEGIN*********************//
        endpoints.tokenStore(new JdbcTokenStore(dataSource));
        //*************** 使用JDBC方式 END *********************//

        //*************** 使用JWT方式 BEGIN*********************//
//        endpoints.tokenStore(tokenStore());
//        endpoints.accessTokenConverter(jwtAccessTokenConverter());
        //*************** 使用JWT方式 END *********************//

    }

    //*************** 使用JWT方式 BEGIN*********************//
//    @Bean
//    public TokenStore tokenStore() {
//        TokenStore tokenStore = new JwtTokenStore(jwtAccessTokenConverter());
//        return tokenStore;
//    }
//
//    private JwtAccessTokenConverter jwtAccessTokenConverter() {
//        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter() {
//            @Override
//            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//                Authentication userAuthentication = authentication.getUserAuthentication();
//                String userName = null;
//
//                if (userAuthentication != null) {
//                    userName = userAuthentication.getName();
//                }
//
//                /** 自定义一些token属性 ***/
//                final Map<String, Object> additionalInformation = new HashMap<>();
//                additionalInformation.put("userName", userName);
//                additionalInformation.put("test_key", "test_value");
//                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
//                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);
//                return enhancedToken;
//            }
//        };
//
////        jwtAccessTokenConverter.setSigningKey("123");   //测试用
//        //RSA方式
//        jwtAccessTokenConverter.setSigningKey(jwtPrivateKey);
//
//        //这个也需要，使用refresh_token刷新的时候，需要校验
//        jwtAccessTokenConverter.setVerifier(new RsaVerifier(jwtPublicKey));
//
//        return jwtAccessTokenConverter;
//    }
    //*************** 使用JWT方式 END *********************//
}
