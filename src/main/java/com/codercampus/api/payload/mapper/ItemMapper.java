package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.*;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import com.codercampus.api.payload.response.responsedto.ItemResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring",uses = {
        UnitTypeMapper.class,
        ItemCategoryMapper.class,
        Expense.class,
})
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "unitType.id", target = "unitTypeId")
    @Mapping(source = "itemCategory.id", target = "itemCategoryId")
    @Mapping(source = "expenses", target = "expenseIds")
    ItemResponseDto toResponseDto(Item expense);

    Set<ItemResponseDto> expensesToResponseDto(Set<Item> items);

    default Long toExpenseId(Expense expense){
        return expense.getId();
    }
}

