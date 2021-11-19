package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {ExpenseTracker.class})
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

    @Mapping(source = "expenseTracker.id", target = "expenseTrackerId")
    ExpenseResponseDto toResponseDto(Expense expense);
}

