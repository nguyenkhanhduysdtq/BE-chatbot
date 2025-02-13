package com.example.demo_chatbot.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id ;

    @Column(name = "username")
    String userName;
    String password;
    String name;
    String fullName;
    int role;

    @OneToMany(mappedBy = "user")
    List<InforDocument> inforDocument;
}
