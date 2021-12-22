package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.*;
import com.codercampus.api.model.compositeId.ExpenseItemId;
import com.codercampus.api.payload.response.responsedto.ExpenseItemResponseDto;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring",uses = {
        Item.class,
        Expense.class,
        ItemCategory.class,
        UnitType.class


})
public interface ExpenseItemMapper {
    ExpenseItemMapper INSTANCE = Mappers.getMapper(ExpenseItemMapper.class);

//    @Mapping(source = "expenseItem.id", target = "id")
    ExpenseItemResponseDto toResponseDto(ExpenseItem expenseItem);

    Set<ExpenseItemResponseDto> expensesItemsToResponseDto(Set<ExpenseItem> expenseItems);

    default Long toRowId(ExpenseItemId expenseItemId){
        return expenseItemId.getRowId();
    }
    default Long toExpenseId(ExpenseItemId expenseItemId){
        return expenseItemId.getExpenseId();
    }
    default Long toItemId(ExpenseItemId expenseItemId){
        return expenseItemId.getItemId();
    }

}

