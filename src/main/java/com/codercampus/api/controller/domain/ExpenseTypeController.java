package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expense-type")
@Validated
public class ExpenseTypeController {

    private final UserService userService;
    private final ExpenseTypeMapper expenseTypeMapper;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseTypeService expenseTypeService;
    private final ExpenseTrackerService expenseTrackerService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;
    private final ExpenseService expenseService;

    public ExpenseTypeController(
            UserService userService,
            ExpenseTypeService expenseTypeService,
            ExpenseTypeMapper expenseTypeMapper,
            ExpenseTrackerService expenseTrackerService,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper,
            ExpenseTrackerMapper expenseTrackerMapperMapper,
            ExpenseService expenseService

    ) {
        this.userService = userService;
        this.expenseTypeService = expenseTypeService;
        this.expenseTypeMapper = expenseTypeMapper;
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
     * @throws ResourceNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseTypeResponseDto> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                .createWith(String.format("The requested id (%d) has not been found!",id));

        resourceNFException.setId(id);

        ExpenseType expenseType = this.expenseTypeService.findById(id).orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(this.expenseTypeMapper.toResponseDto(expenseType), HttpStatus.OK);
    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

        ExpenseType expenseType = this.objectMapper.treeToValue(request,ExpenseType.class);
//
//        Long expenseId = request.get("expenseId").asLong();

//        Optional<Expense> expenseOpt = this.expenseService.findById(expenseId);

//        if(expenseOpt.isPresent()){
//            Expense expense = expenseOpt.get();
        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeService.createIfNotExists(expenseType);

        if(expenseTypeOpt.isPresent()){
            return new ResponseEntity<>(this.expenseTypeMapper.toResponseDto(expenseTypeOpt.get()), HttpStatus.CREATED);

        }

        //TODO appropriate error
        return this.errorHandler.handleResourceAlreadyExistError(request.get("name").asText(),expenseType);
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
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {

        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeService.deleteById(id);
        if(expenseTypeOpt.isPresent()){

            //TODO successful feedback
            return new ResponseEntity<>(expenseTypeMapper.toResponseDto(expenseTypeOpt.get()), HttpStatus.OK);

        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
