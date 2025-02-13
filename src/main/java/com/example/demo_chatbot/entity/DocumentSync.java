package com.example.demo_chatbot.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "document_sync" )
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DocumentSync {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String nameDocument;
    String description;
    String date;
    int status ;
    @Column(name = "collection_db")
    String collectionDB;

    @OneToOne
    @JoinColumn(name = "infor_id")
    InforDocument document;

}
