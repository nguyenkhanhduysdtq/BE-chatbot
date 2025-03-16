package com.example.demo_chatbot.dto.document;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequestDTO {
    String question;
    String answer;
    String client_id;

}

