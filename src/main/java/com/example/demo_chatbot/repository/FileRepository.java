package com.example.demo_chatbot.repository;

import com.example.demo_chatbot.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<FileEntity,Integer> {

    @Query("SELECT f.fileName " +
            "FROM FileEntity f " +
            "JOIN f.infor d " +
            "WHERE d.id = :documentID")
    String getNameFile(@Param("documentID") int id);
}
