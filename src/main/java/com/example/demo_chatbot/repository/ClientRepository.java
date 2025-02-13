package com.example.demo_chatbot.repository;

import com.example.demo_chatbot.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientEntity,Integer> {

}
