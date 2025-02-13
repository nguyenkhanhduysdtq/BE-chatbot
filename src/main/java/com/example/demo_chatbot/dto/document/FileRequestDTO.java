package com.example.demo_chatbot.dto.document;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileRequestDTO {

    String fileName;
    int inforId;
}
