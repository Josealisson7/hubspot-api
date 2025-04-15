package br.com.meetime.contactcreation.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class HubSpotAuthService {
    @Value("${spring.authorization_url}")
    private String authorizationUrl;

    @Value("${spring.token_url}")
    private String token_url;

    public String createAuthorizationUrl(String clientId,
                                         String redirectUri,
                                         String scope) throws UnsupportedEncodingException {
        String encodedScopes = URLEncoder.encode(scope, StandardCharsets.UTF_8.toString()).replace("+", "%20");

        return this.authorizationUrl + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&scope=" + encodedScopes;
    }

    public ResponseEntity<String> exchangeAuthorizationCode(String clientId,
                                                            String redirectUri,
                                                            String clientSecret,
                                                            String code
    ) {
        RestTemplate restTemplate = new RestTemplate();

        String requestBody = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&code=" + code;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAcceptCharset(java.util.Collections.singletonList(StandardCharsets.UTF_8));

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                this.token_url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return response;
    }
}
