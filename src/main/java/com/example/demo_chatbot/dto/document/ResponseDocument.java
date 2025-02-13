package com.example.demo_chatbot.dto.document;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class ResponseDocument {

    String answer;
    String timeAnswer;

}
