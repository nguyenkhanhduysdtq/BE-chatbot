package com.example.demo_chatbot.dto.document;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientReponseDTO {
    String id;
    String name;
    long phone;
    String email;
    String createDate;
    int numberQuestion;

}
