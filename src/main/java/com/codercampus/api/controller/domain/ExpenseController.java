package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandler;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.mapper.ExpenseMapper;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseResponseDto;
import com.codercampus.api.payload.response.responsedto.ExpenseTrackerResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.resource.ExpenseService;
import com.codercampus.api.service.resource.ExpenseTrackerService;
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
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseService expenseService;
    private final ExpenseTrackerService expenseTrackerService;
    private final GlobalErrorHandler errorHandler;
    private final ObjectMapper objectMapper;


    public ExpenseController(
            UserService userService,
            ExpenseService expenseService,
            ExpenseMapper expenseMapper,
            ExpenseTrackerService expenseTrackerService,
            GlobalErrorHandler globalErrorHandler,
            ObjectMapper objectMapper,
            ExpenseTrackerMapper expenseTrackerMapperMapper
    ) {
        this.userService = userService;
        this.expenseService = expenseService;
        this.expenseMapper = expenseMapper;
        this.errorHandler = globalErrorHandler;
        this.objectMapper = objectMapper;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapperMapper;
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
     * @throws ResourceNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDto> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                .createWith(String.format("The requested id (%d) has not been found!",id));

        resourceNFException.setId(id);

        Expense expense = this.expenseService.findById(id).orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(this.expenseMapper.toResponseDto(expense), HttpStatus.OK);
    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> createExpenses(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

        Expense expense = this.objectMapper.treeToValue(request,Expense.class);

        Long expenseTrackerId = request.get("expenseTrackerId").asLong();

            Optional<Expense> expenseOpt = this.expenseService.createIfNotExists(expense,expenseTrackerId);

            if(expenseOpt.isPresent()){
                return new ResponseEntity<>(this.expenseMapper.toResponseDto(expenseOpt.get()), HttpStatus.CREATED);

            }
            return this.errorHandler.handleResourceAlreadyExistError(request.get("name").asText(),expense);
    }

//    /**
//     *
//     * @param request
//     * @return
//     * @throws JsonProcessingException
//     */
//    @PatchMapping
//    public ResponseEntity<?> updatedExpenses(@Valid @RequestBody JsonNode request) throws JsonProcessingException {
//
//        Optional<User> userOpt = this.userService.findById(request.get("userId").asLong());
//        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(request.get("mainCategoryId").asLong());
//
//        ExpenseTracker expenseTracker = this.objectMapper.treeToValue(request,ExpenseTracker.class);
//
//        if (userOpt.isPresent() && mainCategoryOpt.isPresent()){
//            if(this.expenseTrackerService.isExists(expenseTracker.getName())){
//                return this.errorHandler.handleResourceAlreadyExistError(expenseTracker.getName(),expenseTracker);
//            }
//            ExpenseTracker updatedExpenseTracker = this.expenseTrackerService.updatedExpenseTracker(expenseTracker,mainCategoryOpt.get(),userOpt.get());
//            return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(updatedExpenseTracker), HttpStatus.OK);
//        }
//        return this.errorHandler.handleResourceNotUpdatedError(expenseTracker.getName(),expenseTracker);
//
//    }
//
    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {

        Optional<Expense> expenseOpt = this.expenseService.deleteById(id);
        if(expenseOpt.isPresent()){
            //TODO successful feedback
            return new ResponseEntity<>(expenseMapper.toResponseDto(expenseOpt.get()), HttpStatus.OK);

        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
