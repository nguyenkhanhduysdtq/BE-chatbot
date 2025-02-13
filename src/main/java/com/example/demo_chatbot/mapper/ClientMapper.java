package com.example.demo_chatbot.mapper;

import com.example.demo_chatbot.dto.document.ClientDTO;
import com.example.demo_chatbot.entity.ClientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientEntity toEntity(ClientDTO dto);

    ClientDTO toDTO(ClientEntity entity);
}
