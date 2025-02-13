package com.example.demo_chatbot.index;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DocumentStore {

    private String id;
    private String description;
    private String store;
    private float[] embedding; // Nếu embedding là một vector
}
