package com.shop4me.productdatastream.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.shop4me.productdatastream.domain.model.request.toolset.RequestPayloadReader.OBJECT_MAPPER;
import static java.util.Map.entry;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j

@AllArgsConstructor
public class Shop4MeAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final DaoAuthenticationProvider authenticationProvider;

    private final String jwtSignature;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        var username = request.getParameter("username");
        var pass = request.getParameter("password");
        var token = new UsernamePasswordAuthenticationToken(username, pass);
        return authenticationProvider.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        org.springframework.security.core.userdetails.User user = (User) authResult.getPrincipal();
        log.info("Successful authentication of user: '{}'", user.getUsername());

        var algorithm = Algorithm.HMAC256(jwtSignature.getBytes());
        var accessToken = writeAccessToken(user, algorithm);

        var tokens = Map.ofEntries(
                entry("access_token", accessToken)
        );

        response.setContentType(APPLICATION_JSON_VALUE);
        OBJECT_MAPPER.writeValue(response.getOutputStream(), tokens);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.warn("Wrong logging credentials, access forbidden");
        var tokens = new HashMap<>();
        tokens.put("access_token", null);
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        OBJECT_MAPPER.writeValue(response.getOutputStream(), tokens);
    }

    private String writeAccessToken(org.springframework.security.core.userdetails.User user, Algorithm algorithm){
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuer("Shop4Me_Product_Data_Stream_Component")
                .withClaim("roles",
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                )
                .sign(algorithm);
    }
}
