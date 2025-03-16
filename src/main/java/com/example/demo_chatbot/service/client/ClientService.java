package com.example.demo_chatbot.service.client;

import com.example.demo_chatbot.dto.document.ClientDTO;
import com.example.demo_chatbot.dto.document.ClientReponseDTO;
import com.example.demo_chatbot.dto.document.ClientRequestDTO;
import com.example.demo_chatbot.entity.ClientEntity;
import com.example.demo_chatbot.mapper.ClientMapper;
import com.example.demo_chatbot.repository.ClientRepository;
import com.example.demo_chatbot.repository.QuestionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ClientService implements IClientService{

    ClientRepository clientRepository;
    QuestionRepository questionRepository;


    @Override
    public ClientDTO createClient(ClientRequestDTO requestDTO) {
        ClientEntity entity = ClientEntity.builder()
                .id(requestDTO.getId())
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .phone(requestDTO.getPhone())
                .createDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
        return ClientMapper.INSTANCE.toDTO(clientRepository.save(entity));
    }

    @Override
    public List<ClientReponseDTO> getAllClient() {

        List<ClientEntity> listEntity = clientRepository.findAll();
        List<ClientReponseDTO> listDTO = new ArrayList<>();

        for(ClientEntity entity : listEntity){
            int numberCount = questionRepository.numberQuestioonClient(entity.getId());
            ClientReponseDTO dto = ClientReponseDTO.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .phone(entity.getPhone())
                    .createDate(entity.getCreateDate())
                    .email(entity.getEmail())
                    .numberQuestion(numberCount)
                    .build();

            listDTO.add(dto);
        }
        return listDTO;
    }
}
