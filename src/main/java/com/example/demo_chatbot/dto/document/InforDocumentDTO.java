package com.example.demo_chatbot.dto.document;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InforDocumentDTO {

    int id;
    String nameDocument;
    String description;
    String date;
    int status ;

}
