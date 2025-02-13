package com.example.demo_chatbot.index;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DocumentBasic {

    private String id;

    private String content;

    private float[] embedding;
}
