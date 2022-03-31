package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseTrackerResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.ExpenseTrackerService;
import com.codercampus.api.service.domain.MainCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.resource.ResourceException;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expense-tracker")
@CrossOrigin(origins = "*", maxAge = 3600)
@Validated
public class ExpenseTrackerController {

    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseTrackerService expenseTrackerService;
    private final MainCategoryService mainCategoryService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;


    public ExpenseTrackerController(
            ExpenseTrackerService expenseTrackerService,
            ExpenseTrackerMapper expenseTrackerMapper,
            MainCategoryService mainCategoryService,
            GlobalErrorHandlerService globalErrorHandler,
            ObjectMapper objectMapper) {
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapper;
        this.mainCategoryService = mainCategoryService;
        this.errorHandler = globalErrorHandler;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ExpenseTrackerResponseDto>>getAll() {

        List<ExpenseTracker> expenseTrackerCollection = this.expenseTrackerService.findAll();
        return new ResponseEntity<>(expenseTrackerCollection
                .stream()
                .sorted(Comparator.comparing(ExpenseTracker::getCreatedAt).reversed())
                .map(expenseTrackerMapper::toResponseDto)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     * @throws NumberFormatException
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {

        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(id);

        if(expenseTrackerOpt.isPresent()){
            return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(expenseTrackerOpt.get()), HttpStatus.OK);
        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(),null);

    }

    /**
     *
     * @param expenseTrackerRequest
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody JsonNode expenseTrackerRequest) throws JsonProcessingException {

        ExpenseTracker expenseTracker = this.objectMapper.treeToValue(expenseTrackerRequest,ExpenseTracker.class);
//        MainCategory mainCategory = this.objectMapper.treeToValue(expenseTrackerRequest,ExpenseTracker.class);

//        Long mainCategoryId = expenseTrackerRequest.get("mainCategory").get("id").asLong();

        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.createIfNotExists(expenseTracker);

        if(expenseTrackerOpt.isPresent()){
            return new ResponseEntity<>(Collections.singletonList(this.expenseTrackerMapper.toResponseDto(expenseTrackerOpt.get())), HttpStatus.CREATED);

        }

        return this.errorHandler.handleResourceAlreadyExistError(expenseTrackerRequest.get("name").asText(),expenseTracker);

    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PatchMapping
    public ResponseEntity<?> update(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(request.get("mainCategoryId").asLong());

        ExpenseTracker expenseTracker = this.objectMapper.treeToValue(request,ExpenseTracker.class);

        if (mainCategoryOpt.isPresent()){
//            if(this.expenseTrackerService.isExists(expenseTracker.getName())){
//                return this.errorHandler.handleResourceAlreadyExistError(expenseTracker.getName(),expenseTracker);
//            }
            ExpenseTracker updatedExpenseTracker = this.expenseTrackerService.updatedExpenseTracker(expenseTracker,mainCategoryOpt.get());
            return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(updatedExpenseTracker), HttpStatus.OK);
        }
        return this.errorHandler.handleResourceNotUpdatedError(expenseTracker.getName(),expenseTracker);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) throws ResourceHasReferenceException, ResourceException, ResourceNotFoundException {

        ExpenseTracker deletedExpenseTracker = this.expenseTrackerService.deleteById(id);
        return new ResponseEntity<>(expenseTrackerMapper.toResponseDto(deletedExpenseTracker), HttpStatus.OK);

    }

}
