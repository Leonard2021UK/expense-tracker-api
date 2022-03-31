package com.codercampus.api.controller.domain;


import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceNotCreatedException;
import com.codercampus.api.model.*;
import com.codercampus.api.model.compositeId.ExpenseItemId;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/expense-item")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ExpenseItemController {

    private final ObjectMapper objectMapper;
    private final ExpenseItemService expenseItemService;
    private final ExpenseService expenseService;
    private final ItemService itemService;
    private final ItemCategoryService itemCategoryService;
    private final UnitTypeService unitTypeService;
    private final UserService userService;
    private final GlobalErrorHandlerService errorHandler;


    public ExpenseItemController(
            ObjectMapper objectMapper,
            ExpenseItemService expenseItemService,
            ExpenseService expenseService,
            ItemService itemService,
            ItemCategoryService itemCategoryService,
            UnitTypeService unitTypeService,
            UserService userService,
            GlobalErrorHandlerService globalErrorHandlerService
    ) {
        this.objectMapper = objectMapper;
        this.expenseItemService = expenseItemService;
        this.expenseService = expenseService;
        this.itemService = itemService;
        this.itemCategoryService = itemCategoryService;
        this.unitTypeService = unitTypeService;
        this.userService = userService;
        this.errorHandler = globalErrorHandlerService;

    }

    /**
     *
     * @param expenseItemRequest
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody JsonNode expenseItemRequest) throws JsonProcessingException, ResourceNotCreatedException {

        System.out.println(expenseItemRequest.get("expenseForm").asText());
//        Expense[] expenseItems = this.objectMapper.treeToValue(expenseItemRequest.get("expenseForm"), Expense[].class);

//        JsonNode expenseItems = expenseItemRequest.get("items");
//        Long expenseId = expenseItemRequest.get("expenseId").asLong();
//
//        Optional<Expense> expenseOptional = this.expenseService.findById(expenseId);
//        UserDetailsImpl userDetails = this.userService.getUserDetails();
//
//        if(expenseOptional.isPresent()){
//
//            Expense expense = expenseOptional.get();
//
//            List<ExpenseItem> expenseItemList = new ArrayList<>();
//
//
//            expenseItems.forEach(expenseItem->{
//
//                Long itemCategoryId = expenseItem.get("itemCategoryId").asLong();
//                Long itemId = expenseItem.get("itemId").asLong();
//                Long unitTypeId = expenseItem.get("unitTypeId").asLong();
//                Long rowId = expenseItem.get("rowId").asLong();
//
//                Optional<Item> itemOpt = this.itemService.findById(itemId);
//                Optional<ItemCategory> itemCategoryOpt = this.itemCategoryService.findById(itemCategoryId);
//                Optional<UnitType> unitTypeOpt = this.unitTypeService.findById(unitTypeId);
//
//                if(itemOpt.isPresent() && itemCategoryOpt.isPresent() && unitTypeOpt.isPresent()){
//
//                    ExpenseItem expenseItemEntity = new ExpenseItem(expense,itemOpt.get(),rowId);
//
////                    expenseItemEntity.setItem(itemOpt.get());
////                    expenseItemEntity.setExpense(expense);
//
//                    expenseItemEntity.setItemCategory(itemCategoryOpt.get());
//                    expenseItemEntity.setUnitType(unitTypeOpt.get());
//                    expenseItemEntity.setAmount(expenseItem.get("amount").decimalValue());
//                    expenseItemEntity.setUnitPrice(expenseItem.get("unitPrice").decimalValue());
//
//                    expenseItemEntity.setPrice(expenseItem.get("price").decimalValue());
//
//                    expenseItemEntity.setCreatedBy(userDetails.getUsername());
//                    expenseItemEntity.setUpdatedBy(userDetails.getUsername());
//
////                    expense.addItem(expenseItemEntity);
////                    itemOpt.get().addExpense(expenseItemEntity);
//
//                    expenseItemList.add(expenseItemEntity);
//                }
//            });
//
//            this.expenseItemService.saveAll(expenseItemList);
            return new ResponseEntity<>("expenseItems",HttpStatus.OK);
//        }

//        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseItemService.createIfNotExists(expenseTracker,mainCategoryId);
//
//        if(expenseTrackerOpt.isPresent()){
//            return new ResponseEntity<>(Collections.singletonList(this.expenseTrackerMapper.toResponseDto(expenseTrackerOpt.get())), HttpStatus.CREATED);
//
//        }
//        return this.errorHandler.handleResourceNotCreatedError("Expense");

//        throw ResourceNotCreatedException.createWith("Expense could not be found!");
    }

}
