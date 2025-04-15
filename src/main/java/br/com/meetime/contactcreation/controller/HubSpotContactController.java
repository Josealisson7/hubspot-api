package br.com.meetime.contactcreation.controller;

import br.com.meetime.contactcreation.dto.HubSpotContactRequestDto;
import br.com.meetime.contactcreation.service.HubSpotContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contact")
public class HubSpotContactController {

    public HubSpotContactService hubSpotContactService;

    public HubSpotContactController(HubSpotContactService hubSpotContactService) {
        this.hubSpotContactService = hubSpotContactService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createContact(
            @RequestBody HubSpotContactRequestDto contactData,
            @RequestHeader("authorization_token") String authorizationToken) {
        return this.hubSpotContactService.createContact(contactData, authorizationToken);
    }

    @PostMapping("/creation/webhook")
    public ResponseEntity<String> handleContactCreation(@RequestBody List<Map<String, Object>> events) {
        return this.hubSpotContactService.contactCreationWebhook(events);
    }

}
