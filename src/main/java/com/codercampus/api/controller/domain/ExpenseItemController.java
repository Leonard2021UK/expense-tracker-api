package com.codercampus.api.controller.domain;


import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseItem;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.service.domain.ExpenseItemService;
import com.codercampus.api.service.domain.ExpenseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/expense-item")
@Validated
public class ExpenseItemController {

    private final ObjectMapper objectMapper;
    private final ExpenseItemService expenseItemService;
    private final ExpenseService expenseService;

    public ExpenseItemController(ObjectMapper objectMapper, ExpenseItemService expenseItemService, ExpenseService expenseService) {
        this.objectMapper = objectMapper;
        this.expenseItemService = expenseItemService;
        this.expenseService = expenseService;
    }





    /**
     *
     * @param expenseItemRequest
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody JsonNode expenseItemRequest) throws JsonProcessingException {

        ExpenseItem[] expenseItems = this.objectMapper.treeToValue(expenseItemRequest.get("items"), ExpenseItem[].class);
        Long expenseId = expenseItemRequest.get("expenseId").asLong();
//        Long mainCategoryId = expenseItemRequest.get("mainCategoryId").asLong();
        Optional<Expense> expenseOptional = this.expenseService.findById(expenseId);
        if(expenseOptional.isPresent()){
            List<ExpenseItem> expenseItemList = new ArrayList<>();
            Arrays.stream(expenseItems).forEach(expenseItem->{

            });
        }



        this.expenseItemService.saveAll(Arrays.asList(expenseTracker));
//        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseItemService.createIfNotExists(expenseTracker,mainCategoryId);
//
//        if(expenseTrackerOpt.isPresent()){
//            return new ResponseEntity<>(Collections.singletonList(this.expenseTrackerMapper.toResponseDto(expenseTrackerOpt.get())), HttpStatus.CREATED);
//
//        }
//
//        return this.errorHandler.handleResourceAlreadyExistError(expenseItemRequest.get("name").asText(),expenseTracker);
        return new ResponseEntity<>("hey",HttpStatus.OK);
    }

}
