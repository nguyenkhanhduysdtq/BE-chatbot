package com.example.demo_chatbot.service.client;

import com.example.demo_chatbot.dto.document.ClientDTO;
import com.example.demo_chatbot.dto.document.ClientRequestDTO;

public interface IClientService {
    ClientDTO createClient(ClientRequestDTO requestDTO);
}
