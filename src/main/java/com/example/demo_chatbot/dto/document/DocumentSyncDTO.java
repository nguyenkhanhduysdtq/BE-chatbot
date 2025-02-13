package com.example.demo_chatbot.dto.document;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DocumentSyncDTO {

    int id;
    String nameDocument;
    String description;
    String date;
    int status ;
    String collectionDB;
    int inforId;

}
