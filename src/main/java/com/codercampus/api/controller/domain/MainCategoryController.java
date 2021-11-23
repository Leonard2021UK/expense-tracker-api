package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandler;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.mapper.CycleAvoidingMappingContext;
import com.codercampus.api.payload.request.requestdto.MainCategoryRequestDto;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import com.codercampus.api.payload.mapper.MainCategoryMapper;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.MainCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final GlobalErrorHandler errorHandler;
    private final ObjectMapper objectMapper;

    public MainCategoryController(
            UserService userService,
            MainCategoryService mainCategoryService,
            GlobalErrorHandler globalErrorHandler,
            ObjectMapper objectMapper
    ) {
        this.userService = userService;
        this.mainCategoryService = mainCategoryService;
        this.errorHandler = globalErrorHandler;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<MainCategoryResponseDto>> getAll() {

        List<MainCategory> mainCategoryCollection = this.mainCategoryService.findAll();
        return new ResponseEntity<>(mainCategoryCollection
                .stream()
                .map(MainCategoryMapper.INSTANCE::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     * @throws NumberFormatException
     * @throws ResourceNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(id);

        if(mainCategoryOpt.isPresent()){
            return new ResponseEntity<>(MainCategoryMapper.INSTANCE.toResponseDto(mainCategoryOpt.get()), HttpStatus.OK);
        }

        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

    /**
     *
     * @param mainCategoryRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MainCategory mainCategoryRequest) {

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.createIfNotExists(mainCategoryRequest);

        // if mainCategory is not present a record with the same name already exists
        // hence no new record was created
        if(mainCategoryOpt.isPresent()){
            return new ResponseEntity<>(MainCategoryMapper.INSTANCE.toResponseDto(mainCategoryOpt.get()), HttpStatus.CREATED);
        }
        return this.errorHandler.handleResourceAlreadyExistError(mainCategoryRequest.getName(),mainCategoryRequest);

    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PatchMapping
    public ResponseEntity<?> update(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

       Optional<User> userOpt = this.userService.findById(request.get("userId").asLong());

       MainCategory newMainCategory = this.objectMapper.treeToValue(request,MainCategory.class);
        System.out.println(request);
       if(userOpt.isPresent()){

           // if the new main category name exist then return a corresponding error
           if(this.mainCategoryService.isExists(newMainCategory.getName())){
                return this.errorHandler.handleResourceAlreadyExistError(newMainCategory.getName(),newMainCategory);
           }

           MainCategory updatedMainCategory = this.mainCategoryService.update(newMainCategory,userOpt.get());

           return new ResponseEntity<>(MainCategoryMapper.INSTANCE.toResponseDto(updatedMainCategory), HttpStatus.OK);
       }
//           return new ResponseEntity<>(this.mainCategoryMapper.fromMainCategoryRequestDto(request,new CycleAvoidingMappingContext()), HttpStatus.OK);

        return this.errorHandler.handleResourceNotUpdatedError(newMainCategory.getName(),newMainCategory);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.deleteById(id);
        if(mainCategoryOpt.isPresent()){
            //TODO successful feedback
            return new ResponseEntity<>(MainCategoryMapper.INSTANCE.toResponseDto(mainCategoryOpt.get()), HttpStatus.OK);

        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);
    }


}
