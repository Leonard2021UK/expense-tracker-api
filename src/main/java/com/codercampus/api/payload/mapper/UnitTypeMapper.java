package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.model.Item;
import com.codercampus.api.model.UnitType;
import com.codercampus.api.payload.response.responsedto.ExpenseAddressResponseDto;
import com.codercampus.api.payload.response.responsedto.ItemResponseDto;
import com.codercampus.api.payload.response.responsedto.UnitTypeResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(componentModel = "spring",uses = {ItemMapper.class})
public interface UnitTypeMapper {
    UnitTypeMapper INSTANCE = Mappers.getMapper(UnitTypeMapper.class);

//    @Mapping(source = "items", target = "itemIds")
    UnitTypeResponseDto toResponseDto(UnitType unitType);

    default Long toItemId(Item item){
        return item.getId();
    }

    Set<ItemResponseDto> expensesToResponseDto(Set<Item> items);



}

