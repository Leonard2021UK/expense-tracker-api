package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.*;
import com.codercampus.api.model.compositeId.ExpenseItemId;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring",uses = {
        ExpenseTracker.class,
        ExpenseType.class,
        ExpenseAddress.class,
        ExpensePaymentType.class,
        ExpenseItem.class
})
public interface ExpenseMapper {
    ExpenseMapper INSTANCE = Mappers.getMapper(ExpenseMapper.class);

//    @Mapping(source = "expenseTracker.id", target = "expenseTrackerId")
    @Mapping(source = "expenseType", target = "expenseType")
    @Mapping(source = "expenseAddress", target = "expenseAddress")
    @Mapping(source = "expensePaymentType", target = "expensePaymentType")
    @Mapping(source = "expenseItems", target = "expenseItems")
    ExpenseResponseDto toResponseDto(Expense expense);

    Set<ExpenseResponseDto> expensesToResponseDto(Set<Expense> expenses);

    default Long toExpenseId(ExpenseItemId expenseItemId){
        return expenseItemId.getExpenseId();
    }

}

