package com.example.demo_chatbot.service.client;

import com.example.demo_chatbot.dto.document.ClientDTO;
import com.example.demo_chatbot.dto.document.ClientRequestDTO;
import com.example.demo_chatbot.entity.ClientEntity;
import com.example.demo_chatbot.mapper.ClientMapper;
import com.example.demo_chatbot.repository.ClientRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ClientService implements IClientService{

    ClientRepository clientRepository;


    @Override
    public ClientDTO createClient(ClientRequestDTO requestDTO) {
        ClientEntity entity = ClientEntity.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .phone(requestDTO.getPhone())
                .createDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
        return ClientMapper.INSTANCE.toDTO(clientRepository.save(entity));
    }
}
