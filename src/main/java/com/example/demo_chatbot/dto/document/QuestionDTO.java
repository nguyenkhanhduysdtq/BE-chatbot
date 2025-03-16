package com.example.demo_chatbot.dto.document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {

    int id;
    String question;
    String answer;
    String date;
    String time;
    String client_id;
}
