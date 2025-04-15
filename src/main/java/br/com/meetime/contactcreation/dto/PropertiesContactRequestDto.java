package br.com.meetime.contactcreation.dto;

public record PropertiesContactRequestDto(String email,
                                          String firstname,
                                          String lastname,
                                          String phone,
                                          String company) {
}
