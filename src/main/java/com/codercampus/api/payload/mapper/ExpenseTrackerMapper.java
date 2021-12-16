package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.payload.response.responsedto.ExpenseTrackerResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {UserMapper.class,MainCategoryMapper.class,ExpenseMapper.class})
public interface ExpenseTrackerMapper {

    ExpenseTrackerMapper INSTANCE = Mappers.getMapper(ExpenseTrackerMapper.class);

    @Mapping(source = "mainCategory", target = "mainCategory")
    @Mapping(source = "expenses", target = "expenses")

//    @Mapping(source = "user.id", target = "user")
    ExpenseTrackerResponseDto toResponseDto(ExpenseTracker expenseTracker);


}

