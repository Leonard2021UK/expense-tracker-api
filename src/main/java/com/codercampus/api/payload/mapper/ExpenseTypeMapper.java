package com.codercampus.api.payload.mapper;


import com.codercampus.api.model.ExpenseType;
import com.codercampus.api.payload.response.responsedto.ExpenseTypeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {ExpenseMapper.class})
public interface ExpenseTypeMapper {
    ExpenseTypeMapper INSTANCE = Mappers.getMapper(ExpenseTypeMapper.class);

    ExpenseTypeResponseDto toResponseDto(ExpenseType expenseType);
}

