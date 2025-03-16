package com.example.demo_chatbot.service.client;

import com.example.demo_chatbot.dto.document.ClientDTO;
import com.example.demo_chatbot.dto.document.ClientReponseDTO;
import com.example.demo_chatbot.dto.document.ClientRequestDTO;

import java.util.List;

public interface IClientService {
    ClientDTO createClient(ClientRequestDTO requestDTO);
    List<ClientReponseDTO> getAllClient();
}
