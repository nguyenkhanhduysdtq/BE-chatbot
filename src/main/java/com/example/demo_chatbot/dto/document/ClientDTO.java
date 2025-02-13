package com.example.demo_chatbot.dto.document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientDTO {
    int id;
    String name;
    long phone;
    String email;
    String createDate;

}
