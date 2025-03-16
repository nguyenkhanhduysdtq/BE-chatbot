package com.example.demo_chatbot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Table(name = "client")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientEntity {
    @Id
    String id;
    String name;
    long phone;
    String email;
    String createDate;

    @OneToMany(mappedBy = "client")
    List<InforQuestionEntity> question;
}
