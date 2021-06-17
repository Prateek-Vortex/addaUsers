package com.prateek.addausers.addaUsers.addaSecurity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prateek.addausers.addaUsers.addaModel.LoginRequestModel;
import com.prateek.addausers.addaUsers.addaService.UsersService;
import com.prateek.addausers.addaUsers.addaShared.UserDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    Environment environment;
    UsersService usersService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, Environment environment, UsersService usersService) {
        super(authenticationManager);
        this.environment = environment;
        this.usersService = usersService;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try{
            LoginRequestModel loginRequestModel=new ObjectMapper().readValue(request.getInputStream(),LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestModel.getEmail(),
                            loginRequestModel.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
     String userName=((User)authResult.getPrincipal()).getUsername();

        UserDto userDto=usersService.getUserByEmail(userName);

    String token= Jwts.builder()
            .setSubject(userDto.getUserId())
            .setExpiration(new Date(System.currentTimeMillis()+
                    Long.parseLong(environment.getProperty("token.expiration.time"))))
            .signWith(SignatureAlgorithm.HS512,environment.getProperty("token.secret"))
            .compact();

    response.addHeader(HttpHeaders.AUTHORIZATION,token);
    response.addHeader("UserId",userDto.getUserId());
    }
}
