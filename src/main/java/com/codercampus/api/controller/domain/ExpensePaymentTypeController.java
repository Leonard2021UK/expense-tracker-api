package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.ExpensePaymentType;
import com.codercampus.api.payload.mapper.ExpensePaymentTypeMapper;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.response.responsedto.ExpensePaymentTypeResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.ExpensePaymentTypeService;
import com.codercampus.api.service.domain.ExpenseService;
import com.codercampus.api.service.domain.ExpenseTrackerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expense-payment-type")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ExpensePaymentTypeController {

    private final UserService userService;
    private final ExpensePaymentTypeMapper expensePaymentTypeMapper;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpensePaymentTypeService expensePaymentTypeService;
    private final ExpenseTrackerService expenseTrackerService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;
    private final ExpenseService expenseService;



    public ExpensePaymentTypeController(
            UserService userService,
            ExpensePaymentTypeService expensePaymentTypeService,
            ExpensePaymentTypeMapper expensePaymentTypeMapper,
            ExpenseTrackerService expenseTrackerService,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper,
            ExpenseTrackerMapper expenseTrackerMapperMapper,
            ExpenseService expenseService

    ) {
        this.userService = userService;
        this.expensePaymentTypeService = expensePaymentTypeService;
        this.expensePaymentTypeMapper = expensePaymentTypeMapper;
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
    public ResponseEntity<List<ExpensePaymentTypeResponseDto>>getAll() {

        List<ExpensePaymentType> expensePaymentTypeCollection = this.expensePaymentTypeService.findAll();
        return new ResponseEntity<>(expensePaymentTypeCollection
                .stream()
                .map(expensePaymentTypeMapper::toResponseDto)
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


        Optional<ExpensePaymentType> expensePaymentTypeOpt = this.expensePaymentTypeService.findById(id);

        if(expensePaymentTypeOpt.isPresent()){
            return new ResponseEntity<>(this.expensePaymentTypeMapper.toResponseDto(expensePaymentTypeOpt.get()), HttpStatus.OK);

        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(),null);

    }

    /**
     *
     * @param expensePaymentTypeRequest
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody  ExpensePaymentType expensePaymentTypeRequest) throws JsonProcessingException {

//        ExpensePaymentType expensePaymentType = this.objectMapper.treeToValue(request,ExpensePaymentType.class);
//
//        Long expenseId = request.get("expenseId").asLong();

//        Optional<Expense> expenseOpt = this.expenseService.findById(expenseId);

//        if(expenseOpt.isPresent()){
//            Expense expense = expenseOpt.get();
        Optional<ExpensePaymentType> expensePaymentTypeOpt = this.expensePaymentTypeService.createIfNotExists(expensePaymentTypeRequest);

        if(expensePaymentTypeOpt.isPresent()){
            return new ResponseEntity<>(this.expensePaymentTypeMapper.toResponseDto(expensePaymentTypeOpt.get()), HttpStatus.CREATED);

        }

        //TODO appropriate error
        return this.errorHandler.handleResourceAlreadyExistError(expensePaymentTypeRequest.getName(),expensePaymentTypeRequest);
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
//        Long expensePaymentTypeId": null,
//        Long expensePaymentTypeId": null,

        ExpensePaymentType expensePaymentType = this.objectMapper.treeToValue(request,ExpensePaymentType.class);

//        if (expenseTrackerOpt.isPresent()){
//            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
//            if(this.expensePaymentTypeService.isExists(expensePaymentType.getName())){
//                //TODO error message
//                return this.errorHandler.handleResourceAlreadyExistError(expensePaymentType.getPostCode(),expensePaymentType);
//            }
            ExpensePaymentType updatedExpensePaymentType = this.expensePaymentTypeService.update(expensePaymentType);
            return new ResponseEntity<>(this.expensePaymentTypeMapper.toResponseDto(updatedExpensePaymentType), HttpStatus.OK);
//        }
//        return this.errorHandler.handleResourceNotUpdatedError(expense.getName(),expense);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) throws ResourceHasReferenceException, ResourceNotFoundException {

        ExpensePaymentType expensePaymentType = this.expensePaymentTypeService.deleteById(id);
//        if(expensePaymentTypeOpt.isPresent()){

            //TODO successful feedback
            return new ResponseEntity<>(expensePaymentTypeMapper.toResponseDto(expensePaymentType), HttpStatus.OK);

//        }
//        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
