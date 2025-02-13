package com.example.demo_chatbot.repository;

import com.example.demo_chatbot.entity.InforDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InforDocumentRepository extends JpaRepository<InforDocument,Integer> {

    InforDocument findById(int id);

    @Query("SELECT i \n" +
            "FROM InforDocument i \n" +
            "WHERE i.status =1 and  i.id not in (\n" +
            "SELECT d.document.id\n" +
            "FROM DocumentSync d \n" +
            "WHERE d.document.id IS NOT NULL\n" +
            ") ")
    Optional<List<InforDocument>> findByStatus();

}
