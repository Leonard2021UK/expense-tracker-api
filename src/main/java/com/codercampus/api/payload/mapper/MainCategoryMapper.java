package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.MainCategory;
import com.codercampus.api.payload.request.requestdto.MainCategoryRequestDto;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import com.codercampus.api.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {UserMapper.class, UserService.class})
public interface MainCategoryMapper {
    MainCategoryMapper INSTANCE = Mappers.getMapper(MainCategoryMapper.class);

    @Mapping(source = "user.id", target = "userId")
    MainCategoryResponseDto toResponseDto(MainCategory mainCategory);

    @Mapping(source = "userId", target = "user")
    MainCategory toEntity(MainCategoryRequestDto mainCategoryRequestDto);
}

