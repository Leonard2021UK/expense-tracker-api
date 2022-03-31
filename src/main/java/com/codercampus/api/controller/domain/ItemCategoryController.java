package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.ItemCategory;
import com.codercampus.api.payload.mapper.ItemCategoryMapper;
import com.codercampus.api.payload.response.responsedto.ItemCategoryResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.ItemCategoryService;
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
@RequestMapping("/api/item-category")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ItemCategoryController {

    private final UserService userService;
    private final ItemCategoryService itemCategoryService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;
    private final ItemCategoryMapper itemCategoryMapper;

    public ItemCategoryController(
            UserService userService,
            ItemCategoryService itemCategoryService,
            GlobalErrorHandlerService globalErrorHandler,
            ObjectMapper objectMapper,
            ItemCategoryMapper itemCategoryMapper
    ) {
        this.userService = userService;
        this.itemCategoryService = itemCategoryService;
        this.errorHandler = globalErrorHandler;
        this.objectMapper = objectMapper;
        this.itemCategoryMapper = itemCategoryMapper;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ItemCategoryResponseDto>> findAll() {

        List<ItemCategory> itemCategoryCollection = this.itemCategoryService.findAll();
        return new ResponseEntity<>(itemCategoryCollection
                .stream()
                .map(itemCategoryMapper::toResponseDto)
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

        Optional<ItemCategory> itemCategoryOpt = this.itemCategoryService.findById(id);

        if(itemCategoryOpt.isPresent()){
            return new ResponseEntity<>(itemCategoryMapper.toResponseDto(itemCategoryOpt.get()), HttpStatus.OK);
        }

        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

    /**
     *
     * @param mainCategoryRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ItemCategory mainCategoryRequest) {

        Optional<ItemCategory> itemCategoryOpt = this.itemCategoryService.createIfNotExists(mainCategoryRequest);

        // if mainCategory is not present a record with the same name already exists
        // hence no new record was created
        if(itemCategoryOpt.isPresent()){
            return new ResponseEntity<>(itemCategoryMapper.toResponseDto(itemCategoryOpt.get()), HttpStatus.CREATED);
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

       ItemCategory newItemCategory = this.objectMapper.treeToValue(request,ItemCategory.class);

       // if the new main category name exist then return a corresponding error
       if(this.itemCategoryService.isExists(newItemCategory.getName())){
            return this.errorHandler.handleResourceAlreadyExistError(newItemCategory.getName(), newItemCategory);
       }

       ItemCategory updatedItemCategory = this.itemCategoryService.update(newItemCategory);

       return new ResponseEntity<>(itemCategoryMapper.toResponseDto(updatedItemCategory), HttpStatus.OK);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) throws ResourceHasReferenceException, ResourceNotFoundException {

            ItemCategory itemCategory = this.itemCategoryService.deleteById(id);
//            if(itemCategoryOpt.isPresent()){
                //TODO successful feedback
                return new ResponseEntity<>(itemCategoryMapper.toResponseDto(itemCategory), HttpStatus.OK);

//            }
//            return this.errorHandler.handleResourceNotFoundError(id.toString(), null);


    }


}
