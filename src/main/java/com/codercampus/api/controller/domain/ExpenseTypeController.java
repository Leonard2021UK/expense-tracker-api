package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.ExpenseType;
import com.codercampus.api.payload.mapper.ExpenseTypeMapper;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseTypeResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.ExpenseTypeService;
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
@RequestMapping("/api/expense-type")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ExpenseTypeController {

    private final ExpenseTypeMapper expenseTypeMapper;
    private final ExpenseTypeService expenseTypeService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;

    public ExpenseTypeController(
            ExpenseTypeService expenseTypeService,
            ExpenseTypeMapper expenseTypeMapper,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper

    ) {
        this.expenseTypeService = expenseTypeService;
        this.expenseTypeMapper = expenseTypeMapper;
        this.errorHandler = globalErrorHandlerService;
        this.objectMapper = objectMapper;

    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ExpenseTypeResponseDto>>getAll() {

        List<ExpenseType> expenseTypeCollection = this.expenseTypeService.findAll();
        return new ResponseEntity<>(expenseTypeCollection
                .stream()
                .map(expenseTypeMapper::toResponseDto)
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


        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeService.findById(id);

        if (expenseTypeOpt.isPresent()){
            return new ResponseEntity<>(this.expenseTypeMapper.toResponseDto(expenseTypeOpt.get()), HttpStatus.OK);
        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(),null);

    }

    /**
     *
     * @param expenseTypeRequest
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ExpenseType expenseTypeRequest) throws JsonProcessingException {

//        ExpenseType expenseType = this.objectMapper.treeToValue(request,ExpenseType.class);
//
//        Long expenseId = request.get("expenseId").asLong();

//        Optional<Expense> expenseOpt = this.expenseService.findById(expenseId);

//        if(expenseOpt.isPresent()){
//            Expense expense = expenseOpt.get();
        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeService.createIfNotExists(expenseTypeRequest);

        if(expenseTypeOpt.isPresent()){
            return new ResponseEntity<>(this.expenseTypeMapper.toResponseDto(expenseTypeOpt.get()), HttpStatus.CREATED);

        }

        //TODO appropriate error
        return this.errorHandler.handleResourceAlreadyExistError(expenseTypeRequest.getName(),expenseTypeRequest);
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
//        Long expenseTypeId": null,
//        Long expensePaymentTypeId": null,

        ExpenseType expenseType = this.objectMapper.treeToValue(request,ExpenseType.class);

//        if (expenseTrackerOpt.isPresent()){
//            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
//            if(this.expenseTypeService.isExists(expenseType.getName())){
//                //TODO error message
//                return this.errorHandler.handleResourceAlreadyExistError(expenseType.getPostCode(),expenseType);
//            }
            ExpenseType updatedExpenseType = this.expenseTypeService.update(expenseType);
            return new ResponseEntity<>(this.expenseTypeMapper.toResponseDto(updatedExpenseType), HttpStatus.OK);
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

        ExpenseType expenseType = this.expenseTypeService.deleteById(id);
//        if(expenseTypeOpt.isPresent()){

            //TODO successful feedback
            return new ResponseEntity<>(expenseTypeMapper.toResponseDto(expenseType), HttpStatus.OK);

//        }
//        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
