package com.codercampus.api.payload.mapper;

import com.codercampus.api.model.MainCategory;
import com.codercampus.api.payload.request.requestdto.MainCategoryRequestDto;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import com.codercampus.api.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Set;

@Mapper(componentModel = "spring")
//@Mapper(componentModel = "spring",uses = {ExpenseTrackerMapper.class})
//@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface MainCategoryMapper {

//    @Autowired
//    private UserService userService;

    MainCategoryMapper INSTANCE = Mappers.getMapper(MainCategoryMapper.class);

//    @Mapping(source = "expenseTrackers", target = "expenseTrackers")
    MainCategoryResponseDto toResponseDto(MainCategory mainCategory);


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

