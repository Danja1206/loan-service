package ru.team21.loanservice.service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService extends BaseService {

    private final RestTemplate restTemplate;

    public boolean validateToken(String token) {
        String url = "http://localhost:8081/team21/api/v2/auth/check-token";
        System.out.println(token);

        token = "Bearer " + token;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> response = restTemplate.postForEntity(url, entity, Boolean.class);

        return response.getStatusCode().is2xxSuccessful() && Objects.equals(response.getBody(), true);
    }

    public String extractUsername(String token) {
        String url = "http://localhost:8081/team21/api/v2/auth/check-username";
        HttpHeaders headers = new HttpHeaders();

        token = "Bearer " + token;
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        return response.getBody();
    }

    public Long extractUserId(String token) {
        String url = "http://localhost:8081/team21/api/v2/user/getByJwt";
        HttpHeaders headers = new HttpHeaders();

        token = "Bearer " + token;
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Long> response = restTemplate.postForEntity(url, entity, Long.class);

        System.out.println(response.getStatusCode());

        return response.getBody();
    }


}
