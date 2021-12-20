package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.Item;
import com.codercampus.api.model.ItemCategory;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.payload.response.responsedto.ItemCategoryResponseDto;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",uses = {ItemMapper.class})
//@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface ItemCategoryMapper {

//    @Autowired
//    private UserService userService;

    ItemCategoryMapper INSTANCE = Mappers.getMapper(ItemCategoryMapper.class);

//    @Mapping(source = "items", target = "itemIds")
    ItemCategoryResponseDto toResponseDto(ItemCategory mainCategory);

    default Long toItemId(Item item){
        return item.getId();
    }


//    @Mapping(source = "userId", target = "user")
//    public abstract MainCategory fromMainCategoryRequestDto (MainCategoryRequestDto mainCategoryRequestDto, @Context CycleAvoidingMappingContext context);

//    protected UserResponseDto fromLongToUser (Long userId)throws EntityNotFoundException{
//        return UserMapper.INSTANCE.toResponseDto(this.userService.findById(userId).get());
//    }

//    protected User fromLongToUser (Long userId)throws EntityNotFoundException{
//        return this.userService.findById(userId).get();
//    }
//    abstract MainCategory toRequestDto(MainCategoryRequestDto mainCategoryRequestDto);
}

