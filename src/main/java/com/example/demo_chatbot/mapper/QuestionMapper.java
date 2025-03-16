package com.example.demo_chatbot.mapper;

import com.example.demo_chatbot.dto.document.InforDocumentDTO;
import com.example.demo_chatbot.dto.document.QuestionDTO;
import com.example.demo_chatbot.entity.InforQuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {


    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

    @Mapping(target = "client", ignore = true)
    InforQuestionEntity toEntity(QuestionDTO dto);


    @Mapping(target = "client_id", ignore = true)
    QuestionDTO toDTO(InforQuestionEntity entity);


}
