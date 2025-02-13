package com.example.demo_chatbot.mapper;

import com.example.demo_chatbot.dto.document.DocumentSyncDTO;
import com.example.demo_chatbot.entity.DocumentSync;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DocumentSyncMapper {

    DocumentSyncMapper INSTANCE = Mappers.getMapper(DocumentSyncMapper.class);

    @Mapping(target = "document", ignore = true)
    DocumentSync toEntity(DocumentSyncDTO dto);
    @Mapping(target = "inforId", ignore = true)
    DocumentSyncDTO toDTO(DocumentSync entity);
}
