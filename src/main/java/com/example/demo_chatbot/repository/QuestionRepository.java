package com.example.demo_chatbot.repository;

import com.example.demo_chatbot.dto.document.QuestionDTO;
import com.example.demo_chatbot.entity.InforQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<InforQuestionEntity, Integer> {

    @Query("SELECT COUNT(q.id) " +
            "FROM InforQuestionEntity q " +
            "WHERE q.client.id = :clientId")
    int numberQuestioonClient( @Param("clientId") String id);


    @Query("SELECT q " +
            "FROM InforQuestionEntity q " +
            "WHERE q.client.id = :id " +
            "ORDER BY q.id DESC")
    List<InforQuestionEntity> getQuestionByClientId(@Param("id") String id);
}
