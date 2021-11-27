package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpensePaymentType;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.payload.response.responsedto.ExpensePaymentTypeResponseDto;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {ExpenseMapper.class})
public interface ExpensePaymentTypeMapper {
    ExpensePaymentTypeMapper INSTANCE = Mappers.getMapper(ExpensePaymentTypeMapper.class);

    ExpensePaymentTypeResponseDto toResponseDto(ExpensePaymentType expensePaymentType);
}

