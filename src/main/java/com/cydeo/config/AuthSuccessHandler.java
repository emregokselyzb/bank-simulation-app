package com.cydeo.config;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Configuration
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public  void onAuthentication (HttpServletRequest request, HttpServletResponse response,Authentication authentication)  throws IOException, ServletException{

        Set<String> roles= AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("Admin")){
            response.sendRedirect("/index");
        }
        if (roles.contains("Cashier")){
            response.sendRedirect("/make-transfer");
        }
    }
}
