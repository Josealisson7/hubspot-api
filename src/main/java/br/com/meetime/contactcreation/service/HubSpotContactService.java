package br.com.meetime.contactcreation.service;

import br.com.meetime.contactcreation.dto.HubSpotContactRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class HubSpotContactService {

    @Value("${spring.contacts_api_url}")
    private String contacts_api_url;

    public ResponseEntity<String> createContact(HubSpotContactRequestDto contactData,
                                                String authorizationToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authorizationToken);

        HttpEntity<HubSpotContactRequestDto> requestEntity = new HttpEntity<>(contactData, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                contacts_api_url,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
        return response;
    }

    public ResponseEntity<String> contactCreationWebhook(List<Map<String, Object>> events) {
        System.out.println("Webhook received: " + events);

        for (Map<String, Object> event : events) {
            if ("contact.creation".equals(event.get("eventType"))) {
                System.out.println("New contact created: " + event);
            }
        }

        return ResponseEntity.ok("Successfully processed events.");
    }

}
