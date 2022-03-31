package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.payload.mapper.ExpenseAddressMapper;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseAddressResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.ExpenseAddressService;
import com.codercampus.api.service.domain.ExpenseService;
import com.codercampus.api.service.domain.ExpenseTrackerService;
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
@RequestMapping("/api/expense-address")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ExpenseAddressController {

    private final UserService userService;
    private final ExpenseAddressMapper expenseAddressMapper;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseAddressService expenseAddressService;
    private final ExpenseTrackerService expenseTrackerService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;
    private final ExpenseService expenseService;



    public ExpenseAddressController(
            UserService userService,
            ExpenseAddressService expenseAddressService,
            ExpenseAddressMapper expenseAddressMapper,
            ExpenseTrackerService expenseTrackerService,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper,
            ExpenseTrackerMapper expenseTrackerMapperMapper,
            ExpenseService expenseService

    ) {
        this.userService = userService;
        this.expenseAddressService = expenseAddressService;
        this.expenseAddressMapper = expenseAddressMapper;
        this.errorHandler = globalErrorHandlerService;
        this.objectMapper = objectMapper;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapperMapper;
        this.expenseService = expenseService;

    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ExpenseAddressResponseDto>>getAll() {

        List<ExpenseAddress> expenseAddressCollection = this.expenseAddressService.findAll();
        return new ResponseEntity<>(expenseAddressCollection
                .stream()
                .map(expenseAddressMapper::toResponseDto)
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

        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.findById(id);

        if(expenseAddressOpt.isPresent()){
            return new ResponseEntity<>(this.expenseAddressMapper.toResponseDto(expenseAddressOpt.get()), HttpStatus.OK);
        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(),null);

    }

    /**
     *
     * @param expenseAddressRequest
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ExpenseAddress expenseAddressRequest) throws JsonProcessingException {

//        ExpenseAddress expenseAddress = this.objectMapper.treeToValue(request,ExpenseAddress.class);
//
//        Long expenseId = request.get("expenseId").asLong();

//        Optional<Expense> expenseOpt = this.expenseService.findById(expenseId);

//        if(expenseOpt.isPresent()){
//            Expense expense = expenseOpt.get();
        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.createIfNotExists(expenseAddressRequest);

        if(expenseAddressOpt.isPresent()){
            return new ResponseEntity<>(this.expenseAddressMapper.toResponseDto(expenseAddressOpt.get()), HttpStatus.CREATED);

        }

        //TODO appropriate error
        return this.errorHandler.handleResourceAlreadyExistError(expenseAddressRequest.getName(),expenseAddressRequest);
    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PatchMapping
    public ResponseEntity<?> update(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

//        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(request.get("expenseTrackerId").asLong());
//        Long expenseTrackerId = request.get("expenseTrackerId")
//        Long expenseTypeId
//        Long expenseAddressId": null,
//        Long expensePaymentTypeId": null,

        ExpenseAddress expenseAddress = this.objectMapper.treeToValue(request,ExpenseAddress.class);

//        if (expenseTrackerOpt.isPresent()){
//            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
//            if(this.expenseAddressService.isExists(expenseAddress.getName())){
//                //TODO error message
//                return this.errorHandler.handleResourceAlreadyExistError(expenseAddress.getPostCode(),expenseAddress);
//            }
            ExpenseAddress updatedExpenseAddress = this.expenseAddressService.update(expenseAddress);
            return new ResponseEntity<>(this.expenseAddressMapper.toResponseDto(updatedExpenseAddress), HttpStatus.OK);
//        }
//        return this.errorHandler.handleResourceNotUpdatedError(expense.getName(),expense);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) throws ResourceHasReferenceException, ResourceNotFoundException {

        ExpenseAddress expenseAddress = this.expenseAddressService.deleteById(id);
//        if(expenseAddressOpt.isPresent()){

            //TODO successful feedback
            return new ResponseEntity<>(expenseAddressMapper.toResponseDto(expenseAddress), HttpStatus.OK);

//        }
//        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
