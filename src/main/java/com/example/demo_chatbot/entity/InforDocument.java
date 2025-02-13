package com.example.demo_chatbot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;



@Entity
@Table(name = "document")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InforDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String nameDocument;
    String description;
    String date;
    int status ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    @OneToOne(mappedBy = "infor")
    FileEntity file;
    
    @OneToOne(mappedBy = "document")
    DocumentSync documentSync;
}
