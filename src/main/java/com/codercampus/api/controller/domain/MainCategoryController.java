package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceAlreadyExistException;
import com.codercampus.api.exception.ResourceHasReferenceException;
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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/main-category")
@Validated
public class MainCategoryController {

    private final UserService userService;
    private final MainCategoryService mainCategoryService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;
    private final MainCategoryMapper mainCategoryMapper;

    public MainCategoryController(
            UserService userService,
            MainCategoryService mainCategoryService,
            GlobalErrorHandlerService globalErrorHandler,
            ObjectMapper objectMapper,
            MainCategoryMapper mainCategoryMapper
    ) {
        this.userService = userService;
        this.mainCategoryService = mainCategoryService;
        this.errorHandler = globalErrorHandler;
        this.objectMapper = objectMapper;
        this.mainCategoryMapper = mainCategoryMapper;
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
                .map(mainCategoryMapper::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     * @throws NumberFormatException
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(id);

        if(mainCategoryOpt.isPresent()){
            return new ResponseEntity<>(mainCategoryMapper.toResponseDto(mainCategoryOpt.get()), HttpStatus.OK);
        }

        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

    /**
     *
     * @param mainCategoryRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MainCategory mainCategoryRequest) throws ResourceAlreadyExistException {

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.createIfNotExists(mainCategoryRequest);

        // if mainCategory is not present a record with the same name already exists
        // hence no new record was created
        if(mainCategoryOpt.isPresent()){
            return new ResponseEntity<>(Collections.singletonList(mainCategoryMapper.toResponseDto(mainCategoryOpt.get())), HttpStatus.CREATED);
        }
        //TODO fix error vs exception
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

       MainCategory newMainCategory = this.objectMapper.treeToValue(request,MainCategory.class);

       // if the new main category name exist then return a corresponding error
       if(this.mainCategoryService.isExists(newMainCategory.getName())){
            return this.errorHandler.handleResourceAlreadyExistError(newMainCategory.getName(),newMainCategory);
       }

       MainCategory updatedMainCategory = this.mainCategoryService.update(newMainCategory);

       return new ResponseEntity<>(mainCategoryMapper.toResponseDto(updatedMainCategory), HttpStatus.OK);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) throws ResourceHasReferenceException, ResourceNotFoundException {

        MainCategory mainCategory = this.mainCategoryService.deleteById(id);

//        if(mainCategoryOpt.isPresent()){
            //TODO successful feedback
            return new ResponseEntity<>(mainCategoryMapper.toResponseDto(mainCategory), HttpStatus.OK);

//        }
//        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);
    }


}
