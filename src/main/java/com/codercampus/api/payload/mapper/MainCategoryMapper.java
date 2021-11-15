package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.MainCategory;
import com.codercampus.api.payload.request.requestdto.MainCategoryRequestDto;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MainCategoryMapper {
    MainCategoryMapper INSTANCE = Mappers.getMapper(MainCategoryMapper.class);
    MainCategoryResponseDto toResponseDto(MainCategory mainCategory);
    MainCategory toRequestDto(MainCategoryRequestDto mainCategoryRequestDto);
}
