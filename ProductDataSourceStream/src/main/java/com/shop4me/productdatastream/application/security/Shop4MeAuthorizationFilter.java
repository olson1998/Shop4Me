package com.shop4me.productdatastream.application.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor

@Slf4j

public class Shop4MeAuthorizationFilter extends OncePerRequestFilter {

    private final String jwtSignature;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals("/lg")){
            filterChain.doFilter(request, response);
        }
        else{
            var authHeader = request.getHeader(AUTHORIZATION);

            if(authHeader != null && authHeader.startsWith("Auth ")){
                try{
                    var token = authHeader.substring("Auth ".length());
                    var algorithm = Algorithm.HMAC256(jwtSignature.getBytes());
                    var verifier = JWT.require(algorithm).build();
                    var decodedJWT = verifier.verify(token);
                    var username = decodedJWT.getSubject();
                    var roles = decodedJWT.getClaim("roles").asArray(String.class);
                    var authorities = new ArrayList<SimpleGrantedAuthority>();

                    Arrays.stream(roles).forEach(role ->
                            authorities.add(new SimpleGrantedAuthority(role))
                    );
                    var authToken =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    authorities
                            );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    filterChain.doFilter(request, response);
                }catch (Exception e){
                    var errors = new HashMap<String, String>();
                    var error = e.toString();
                    errors.put("exception", error);
                    response.setStatus(FORBIDDEN.value());
                    response.setHeader("error", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), errors);
                    log.error("Error during auth: {}", error);
                }
            }
            else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
