package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.User;
import com.codercampus.api.payload.response.responsedto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toResponseDto(User user);

//    @Mapping(source = "")
//    User map(Long value);
}

