package com.example.demo_chatbot.mapper;

import com.example.demo_chatbot.dto.document.UserDTO;
import com.example.demo_chatbot.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "inforDocument", ignore = true)
    UserEntity toEntity(UserDTO dto);


    UserDTO toDTO(UserEntity entity);
}
