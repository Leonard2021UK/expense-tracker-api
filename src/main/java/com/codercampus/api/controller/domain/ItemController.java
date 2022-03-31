package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceAlreadyExistException;
import com.codercampus.api.model.*;
import com.codercampus.api.payload.mapper.ExpenseMapper;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.mapper.ItemMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import com.codercampus.api.payload.response.responsedto.ItemResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/item")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ItemController {

    private final UserService userService;
    private final ExpenseMapper expenseMapper;
    private final ItemMapper itemMapper;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseService expenseService;
    private final ItemService itemService;
    private final ExpenseTrackerService expenseTrackerService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;
    private final ExpenseAddressService expenseAddressService;
    private final ExpenseTypeService expenseTypeService;
    private final ExpensePaymentTypeService expensePaymentTypeService;

    public ItemController(
            UserService userService,
            ItemService itemService,
            ExpenseService expenseService,
            ExpenseMapper expenseMapper,
            ExpenseTrackerService expenseTrackerService,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper,
            ExpenseTrackerMapper expenseTrackerMapperMapper,
            ExpenseAddressService expenseAddressService,
            ExpenseTypeService expenseTypeService,
            ExpensePaymentTypeService expensePaymentTypeService,
            ItemMapper itemMapper
            ) {
        this.userService = userService;
        this.itemService = itemService;
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
        this.errorHandler = globalErrorHandlerService;
        this.objectMapper = objectMapper;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapperMapper;
        this.expenseAddressService = expenseAddressService;
        this.expenseTypeService = expenseTypeService;
        this.expensePaymentTypeService = expensePaymentTypeService;
        this.itemMapper = itemMapper;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ItemResponseDto>>getAll() {

        List<Item> itemCollection = this.itemService.findAllNoneArchived();
        return new ResponseEntity<>(itemCollection
                .stream()
                .map(itemMapper::toResponseDto)
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

        //TODO form appropriate error for every findById
        Optional<Item> itemOpt = this.itemService.findById(id);
        if(itemOpt.isPresent()){
            return new ResponseEntity<>(this.itemMapper.toResponseDto(itemOpt.get()), HttpStatus.OK);
        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(),null);

    }

    /**
     *
     * @param itemRequest
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody  Item itemRequest) throws JsonProcessingException {

//        Item item = this.objectMapper.treeToValue(itemRequest,Item.class);

//        Long unitTypeId = request.get("unitTypeId").asLong();
//        Long itemCategoryId = request.get("itemCategoryId").asLong();
//        Long expenseId = request.get("expenseId").asLong();
//        Optional<Item> itemOpt = this.itemService.createIfNotExists(item,unitTypeId,itemCategoryId,expenseId);

        Optional<Item> itemOpt = this.itemService.createIfNotExists(itemRequest);

        if(itemOpt.isPresent()){
            return new ResponseEntity<>(this.itemMapper.toResponseDto(itemOpt.get()), HttpStatus.CREATED);

        }
        return this.errorHandler.handleResourceAlreadyExistError(itemRequest.getName(),itemRequest);
    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PatchMapping
    public ResponseEntity<?> update(@Valid @RequestBody JsonNode request) throws JsonProcessingException {


        Item item = this.objectMapper.treeToValue(request,Item.class);
//        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(request.get("expenseTrackerId").asLong());
//        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.findById(request.get("expenseAddressId").asLong());
//        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeService.findById(request.get("expenseTypeId").asLong());
//        Optional<ExpensePaymentType> expensePaymentTypeOpt = this.expensePaymentTypeService.findById(request.get("expensePaymentTypeId").asLong());
        //TODO refactor into service
//        Expense expense = this.objectMapper.treeToValue(request,Expense.class);

        // if the new main category name exist then return a corresponding error
        if(this.itemService.isExists(item.getName())){
            return this.errorHandler.handleResourceAlreadyExistError(item.getName(),item);
        }

        Item updatedItem = this.itemService.update(item);

        return new ResponseEntity<>(this.itemMapper.toResponseDto(updatedItem), HttpStatus.OK);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {

        Optional<Item> itemOpt = this.itemService.archiveOrDeleteById(id);
        if(itemOpt.isPresent()){
            //TODO successful feedback
            return new ResponseEntity<>(this.itemMapper.toResponseDto(itemOpt.get()), HttpStatus.OK);

        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
