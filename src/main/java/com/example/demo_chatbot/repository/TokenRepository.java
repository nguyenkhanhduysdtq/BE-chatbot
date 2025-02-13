package com.example.demo_chatbot.repository;

import com.example.demo_chatbot.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository  extends JpaRepository<TokenEntity,Integer> {
    boolean existsByJwtId(String jwtId);
}
