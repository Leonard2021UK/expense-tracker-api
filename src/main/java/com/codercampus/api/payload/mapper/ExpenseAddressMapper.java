package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.payload.response.responsedto.ExpenseAddressResponseDto;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring",uses = {ExpenseMapper.class})
public interface ExpenseAddressMapper {
    ExpenseAddressMapper INSTANCE = Mappers.getMapper(ExpenseAddressMapper.class);

    ExpenseAddressResponseDto toResponseDto(ExpenseAddress expenseAddress);


}

