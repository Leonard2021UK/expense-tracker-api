package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.*;
import com.codercampus.api.payload.mapper.ExpenseMapper;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expense")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ExpenseController {

    private final UserService userService;
    private final ExpenseMapper expenseMapper;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseService expenseService;
    private final ExpenseTrackerService expenseTrackerService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;
    private final ExpenseAddressService expenseAddressService;
    private final ExpenseTypeService expenseTypeService;
    private final ExpensePaymentTypeService expensePaymentTypeService;
    private final Validator validator;

    public ExpenseController(
            UserService userService,
            ExpenseService expenseService,
            ExpenseMapper expenseMapper,
            ExpenseTrackerService expenseTrackerService,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper,
            ExpenseTrackerMapper expenseTrackerMapperMapper,
            ExpenseAddressService expenseAddressService,
            ExpenseTypeService expenseTypeService,
            ExpensePaymentTypeService expensePaymentTypeService,
            Validator validator
            ) {
        this.userService = userService;
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
        this.errorHandler = globalErrorHandlerService;
        this.objectMapper = objectMapper;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapperMapper;
        this.expenseAddressService = expenseAddressService;
        this.expenseTypeService = expenseTypeService;
        this.expensePaymentTypeService = expensePaymentTypeService;
        this.validator = validator;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>>getAll() {

        List<Expense> expenseCollection = this.expenseService.findAll();
        return new ResponseEntity<>(expenseCollection
                .stream()
                .map(expenseMapper::toResponseDto)
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
        Optional<Expense> expenseOpt = this.expenseService.findById(id);
        if(expenseOpt.isPresent()){
            return new ResponseEntity<>(this.expenseMapper.toResponseDto(expenseOpt.get()), HttpStatus.OK);
        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(),null);

    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create( @Valid @RequestBody JsonNode request) throws JsonProcessingException {

        Expense expense = this.objectMapper.treeToValue(request.get("expenseForm"),Expense.class);

        Set<ConstraintViolation<Expense>> violations = validator.validate(expense);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
//        Expense expense = this.objectMapper.treeToValue(request,Expense.class);
        ExpenseTracker expenseTracker = this.objectMapper.treeToValue(request.get("expenseForm").get("expenseTracker"),ExpenseTracker.class);
//        List<ExpenseItem> expenseItemsList = Arrays.asList(this.objectMapper.treeToValue(request.get("items"),ExpenseItem[].class));
//        expense.setExpenseTracker(expenseTracker);
        JsonNode expenseItemRowsNode = request.get("items");
//        Long expenseTrackerId = request.get("expenseTrackerId").asLong();
//        Long expenseAddressId = request.get("expenseAddressId").asLong();
//        Long expenseTypeId = request.get("expenseTypeId").asLong();
//        Long expensePaymentTypeId = request.get("expensePaymentTypeId").asLong();
        Optional<Expense> expenseOpt = this.expenseService.createIfNotExists(expense,expenseTracker,expenseItemRowsNode);
//
        if(expenseOpt.isPresent()){
            return new ResponseEntity<>(expenseOpt.get(), HttpStatus.CREATED);
//
        }
        return this.errorHandler.handleResourceAlreadyExistError(request.get("expenseForm").get("expenseName").asText(),expense);
    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PatchMapping
    public ResponseEntity<?> patch(@Valid @RequestBody JsonNode request) throws JsonProcessingException, ResourceNotFoundException {
        Expense expense = this.objectMapper.treeToValue(request.get("expenseForm"),Expense.class);

        ExpenseTracker expenseTracker = this.objectMapper.treeToValue(request.get("expenseForm").get("expenseTracker"),ExpenseTracker.class);
//        ExpensePaymentType expensePaymentType = this.objectMapper.treeToValue(request.get("expenseForm").get("expensePaymentType"),ExpensePaymentType.class);
//        ExpenseType expenseType = this.objectMapper.treeToValue(request.get("expenseForm").get("expenseType"),ExpenseType.class);
//        ExpenseAddress expenseAddress = this.objectMapper.treeToValue(request.get("expenseForm").get("expenseAddress"),ExpenseAddress.class);
//        Item[] items = this.objectMapper.treeToValue(request.get("items"),Item[].class);

//        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(expenseForm.get("expenseTrackerId").asLong());
//        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.findById(request.get("expenseAddressId").asLong());
//        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeService.findById(request.get("expenseTypeId").asLong());
//        Optional<ExpensePaymentType> expensePaymentTypeOpt = this.expensePaymentTypeService.findById(request.get("expensePaymentTypeId").asLong());
        //TODO refactor into service
//        Expense expense = this.objectMapper.treeToValue(request,Expense.class);

//        if (expenseTrackerOpt.isPresent()){
//            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
//            ExpenseAddress expenseAddress = expenseAddressOpt.get();
//            ExpenseType expenseType = expenseTypeOpt.get();
//            ExpensePaymentType expensePaymentType = expensePaymentTypeOpt.get();
//            if(this.expenseService.isExists(expense.getExpenseName())){
//                return this.errorHandler.handleResourceAlreadyExistError(expense.getExpenseName(),expense);
//            }
            Expense updatedExpense = this.expenseService.update(expense,expenseTracker);

            return new ResponseEntity<>(this.expenseMapper.toResponseDto(updatedExpense), HttpStatus.OK);
        }
//        return this.errorHandler.handleResourceNotUpdatedError(expense.getExpenseName(),expense);

//    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) throws ResourceNotFoundException {

        Expense expense = this.expenseService.deleteById(id);
            //TODO successful feedback
            return new ResponseEntity<>(expenseMapper.toResponseDto(expense), HttpStatus.OK);

    }

}
