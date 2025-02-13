package com.example.demo_chatbot.dto.document.authentication;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AuthenticationResponseDTO {
    String token;
    String refreshToken;
    boolean auth;
}
