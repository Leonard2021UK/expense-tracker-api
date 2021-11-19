package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandler;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.mapper.ExpenseMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import com.codercampus.api.payload.response.responsedto.ExpenseTrackerResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.resource.ExpenseService;
import com.codercampus.api.service.resource.MainCategoryService;
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
@RequestMapping("/api/expense")
@Validated
public class ExpenseController {

    private final UserService userService;
    private final ExpenseMapper expenseMapper;
    private final ExpenseService expenseService;
    private final MainCategoryService mainCategoryService;
    private final GlobalErrorHandler errorHandler;
    private final ObjectMapper objectMapper;


    public ExpenseController(
            UserService userService,
            ExpenseService expenseService,
            ExpenseMapper expenseMapper,
            MainCategoryService mainCategoryService,
            GlobalErrorHandler globalErrorHandler,
            ObjectMapper objectMapper) {
        this.userService = userService;
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
        this.mainCategoryService = mainCategoryService;
        this.errorHandler = globalErrorHandler;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDto>>getAllExpenses() {

        List<ExpenseTracker> expenseTrackerCollection = this.expenseService.findAll();
        return new ResponseEntity<>(expenseTrackerCollection
                .stream()
                .map(expenseMapper::toResponseDto)
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
    public ResponseEntity<ExpenseTrackerResponseDto> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                .createWith(String.format("The requested id (%d) has not been found!",id));

        resourceNFException.setId(id);

        ExpenseTracker expenseTracker = this.expenseTrackerService.findById(id).orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(expenseTracker), HttpStatus.CREATED);
    }

    /**
     *
     * @param expenseTrackerRequest
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> createExpenses(@Valid @RequestBody JsonNode expenseRequest) throws JsonProcessingException {

        Expense expense = this.objectMapper.treeToValue(expenseRequest,Expense.class);

        Long expenseTrackerId = expenseRequest.get("expenseTrackerId").asLong();

        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(expenseTrackerId);

        if(expenseTrackerOpt.isPresent()) {

            Optional<Expense> expenseOpt = this.expenseService.createIfNotExists(expenseTracker,mainCategoryId);

            if(expenseTrackerOpt.isPresent()){
                return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(expenseTrackerOpt.get()), HttpStatus.CREATED);

            }

            return this.errorHandler.handleResourceAlreadyExistError(expenseTrackerRequest.get("name").asText(),expenseTracker);
        }
        return this.errorHandler.handleResourceNotCreatedError(expenseTrackerRequest.get("name").asText());
    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PatchMapping
    public ResponseEntity<?> updatedExpenses(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

        Optional<User> userOpt = this.userService.findById(request.get("userId").asLong());
        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(request.get("mainCategoryId").asLong());

        ExpenseTracker expenseTracker = this.objectMapper.treeToValue(request,ExpenseTracker.class);

        if (userOpt.isPresent() && mainCategoryOpt.isPresent()){
            if(this.expenseTrackerService.isExists(expenseTracker.getName())){
                return this.errorHandler.handleResourceAlreadyExistError(expenseTracker.getName(),expenseTracker);
            }
            ExpenseTracker updatedExpenseTracker = this.expenseTrackerService.updatedExpenseTracker(expenseTracker,mainCategoryOpt.get(),userOpt.get());
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
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {

        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.deleteById(id);
        if(expenseTrackerOpt.isPresent()){
            //TODO successful feedback
            return new ResponseEntity<>(expenseTrackerMapper.toResponseDto(expenseTrackerOpt.get()), HttpStatus.OK);

        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
