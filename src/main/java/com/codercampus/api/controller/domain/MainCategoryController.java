package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandler;
import com.codercampus.api.exception.CustomException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import com.codercampus.api.payload.mapper.MainCategoryMapper;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.resource.MainCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/main-category")
@Validated
public class MainCategoryController {

    private final UserService userService;
    private final MainCategoryService mainCategoryService;
    private final MainCategoryMapper mainCategoryMapper;
    private final GlobalErrorHandler errorHandler;
    private final ObjectMapper objectMapper;

    public MainCategoryController(
            UserService userService,
            MainCategoryService mainCategoryService,
            MainCategoryMapper mainCategoryMapper,
            GlobalErrorHandler globalErrorHandler,
            ObjectMapper objectMapper
    ) {
        this.userService = userService;
        this.mainCategoryService = mainCategoryService;
        this.mainCategoryMapper = mainCategoryMapper;
        this.errorHandler = globalErrorHandler;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<?> createMainCategory(@Valid @RequestBody MainCategory mainCategoryRequest) {

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.createIfNotExists(mainCategoryRequest);

        // if mainCategory is not present a record with the same name already exists
        // hence no new record was created
        if(mainCategoryOpt.isPresent()){
            return new ResponseEntity<>(this.mainCategoryMapper.toResponseDto(mainCategoryOpt.get()), HttpStatus.CREATED);
        }
        return this.errorHandler.handleResourceAlreadyExistError(mainCategoryRequest.getName(),mainCategoryRequest);

    }

    @PatchMapping
    public ResponseEntity<?> updateMainCategory(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

       Optional<User> userOpt = this.userService.findById(request.get("userId").asLong());

       MainCategory newMainCategory = this.objectMapper.treeToValue(request,MainCategory.class);

       if(userOpt.isPresent()){

           // if the new main category name exist then return a corresponding error
           if(this.mainCategoryService.isExists(newMainCategory.getName())){
                return this.errorHandler.handleResourceAlreadyExistError(newMainCategory.getName(),newMainCategory);
           }

           MainCategory updatedMainCategory = this.mainCategoryService.updateMainCategory(newMainCategory,userOpt.get());

           return new ResponseEntity<>(this.mainCategoryMapper.toResponseDto(updatedMainCategory), HttpStatus.OK);
       }

        return this.errorHandler.handleResourceNotUpdatedError(newMainCategory.getName(),newMainCategory);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {
//        ResourceNotFoundException resourceNFException =  ResourceNotFoundException
//                .createWith(String.format("The requested id (%d) has not been found!",id));
//        resourceNFException.setId(id);

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(id);

        if(mainCategoryOpt.isPresent()){
            return new ResponseEntity<>(mainCategoryMapper.toResponseDto(mainCategoryOpt.get()), HttpStatus.CREATED);
        }

        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }















    @GetMapping
    public ResponseEntity<List<MainCategoryResponseDto>> getAllMainCategory() {

        List<MainCategory> mainCategoryCollection = this.mainCategoryService.findAll();
        System.out.println("hello");
        return new ResponseEntity<>(mainCategoryCollection
                .stream()
                .map(mainCategoryMapper::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) throws CustomException {

        try{
            this.mainCategoryService.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                    .createWith(String.format("The requested id (%d) has not been found!",id));
            resourceNFException.setId(id);
            throw resourceNFException;
        }

        //TODO successful feedback
        return new ResponseEntity<>("id", HttpStatus.OK);
    }


}
