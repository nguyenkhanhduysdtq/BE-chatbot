package com.example.demo_chatbot.mapper;


import com.example.demo_chatbot.dto.document.InforDocumentDTO;
import com.example.demo_chatbot.entity.InforDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InForMapper {



    InForMapper INSTANCE = Mappers.getMapper(InForMapper.class);

    @Mapping(target = "user", ignore = true)
    InforDocument toEntity(InforDocumentDTO dto);

    InforDocumentDTO toDTO(InforDocument entity);

}
