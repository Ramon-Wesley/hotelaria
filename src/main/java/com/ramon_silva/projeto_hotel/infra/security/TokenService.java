package com.ramon_silva.projeto_hotel.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.ramon_silva.projeto_hotel.models.UsersModel;

@Service
public class TokenService {
    
    @Value("${api.security.token.secret}")
    private String secret;

    public String GeneratedToken(UsersModel user){
        try {
            Algorithm algorithm=Algorithm.HMAC256(secret);
            String token= JWT
            .create()
            .withIssuer("hotelaria-api")
            .withSubject(user.getLogin())
            .withExpiresAt(genExpirationDate())
            .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
          throw new RuntimeException("Error generated Token",e);
        }
    }

    public String validateToken(String token){
        try {
              Algorithm algorithm=Algorithm.HMAC256(secret);
            return JWT
            .require(algorithm)
            .withIssuer("hotelaria-api")
           .build()
           .verify(token)
           .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime
        .now()
        .plusHours(2)
        .toInstant(ZoneOffset.of("-03:00"));
    }
}
