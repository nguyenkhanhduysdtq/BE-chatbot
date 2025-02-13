package com.example.demo_chatbot.mapper;


import com.example.demo_chatbot.dto.document.FileDTO;
import com.example.demo_chatbot.entity.FileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileMapper INSTANCE = Mappers.getMapper(FileMapper.class);

    @Mapping(target = "infor", ignore = true)
    FileEntity toEntity(FileDTO dto);

    FileDTO toDTO(FileEntity file);

}
