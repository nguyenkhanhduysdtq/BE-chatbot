package com.example.demo_chatbot.index;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE )
public class DescriptionBasic {
    String id;
    String description;
    String store;
    float[] embedding;

}
