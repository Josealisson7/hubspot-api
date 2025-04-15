package br.com.meetime.contactcreation.controller;

import br.com.meetime.contactcreation.dto.HubSpotAuthUrlResponseDto;
import br.com.meetime.contactcreation.service.HubSpotAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/authorization")
public class HubSpotAuthController {

    HubSpotAuthService hubSpotAuthService;

    public HubSpotAuthController(HubSpotAuthService hubSpotAuthService) {
        this.hubSpotAuthService = hubSpotAuthService;
    }

    @GetMapping("/url")
    public ResponseEntity<HubSpotAuthUrlResponseDto> requestAuthorization(@RequestParam String clientId,
                                                                          @RequestParam String redirectUri,
                                                                          @RequestParam String scope
    ) throws UnsupportedEncodingException {
        HubSpotAuthUrlResponseDto hubSpotAuthUrlResponseDto = new HubSpotAuthUrlResponseDto(
                HttpStatus.OK.value(),
                this.hubSpotAuthService.createAuthorizationUrl(clientId, redirectUri, scope)
        );
        return ResponseEntity.ok(hubSpotAuthUrlResponseDto);
    }

    @GetMapping("/token")
    public ResponseEntity<String> exchangeAuthorizationCode(@RequestParam String clientId,
                                                            @RequestParam String redirectUri,
                                                            @RequestParam String clientSecret,
                                                            @RequestParam String code) {

        return this.hubSpotAuthService.exchangeAuthorizationCode(clientId, redirectUri, clientSecret, code);
    }

}
