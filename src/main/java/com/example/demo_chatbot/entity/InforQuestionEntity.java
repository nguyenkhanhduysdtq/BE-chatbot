package com.example.demo_chatbot.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InforQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Lob
    @Column(columnDefinition = "mediumtext") // Hoặc LONGTEXT
    String question;
    @Lob
    @Column(columnDefinition = "mediumtext") // Hoặc LONGTEXT
    String answer;
    String date;
    String time;

    @ManyToOne
    @JoinColumn(name = "client_id")
    ClientEntity client;
}
