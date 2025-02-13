package com.example.demo_chatbot.dto.document.introspect;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level =AccessLevel.PRIVATE)
public class IntrospectResponseDTO {
    boolean result;
}
