package br.com.meetime.contactcreation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HubSpotContactRequestDto(@JsonProperty("properties") PropertiesContactRequestDto propertiesContactRequestDto) {
}
