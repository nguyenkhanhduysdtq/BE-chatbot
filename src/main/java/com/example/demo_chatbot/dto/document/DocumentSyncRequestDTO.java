package com.example.demo_chatbot.dto.document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DocumentSyncRequestDTO {

    String nameDocument;
    String description;
}
