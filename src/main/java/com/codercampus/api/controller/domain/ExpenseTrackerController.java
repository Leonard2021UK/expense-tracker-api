package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandler;
import com.codercampus.api.exception.CustomException;
import com.codercampus.api.exception.ResourceNotCreatedException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.exception.ResourceNotUpdatedException;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.mapper.MainCategoryMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseTrackerResponseDto;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.resource.ExpenseTrackerService;
import com.codercampus.api.service.resource.MainCategoryService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expense-tracker")
@Validated
public class ExpenseTrackerController {

    private final UserService userService;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseTrackerService expenseTrackerService;
    private final MainCategoryService mainCategoryService;
    private final GlobalErrorHandler errorHandler;

    public ExpenseTrackerController(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseTrackerMapper expenseTrackerMapper,
            MainCategoryService mainCategoryService,
            GlobalErrorHandler globalErrorHandler
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapper;
        this.mainCategoryService = mainCategoryService;
        this.errorHandler = globalErrorHandler;
    }

    @PostMapping
    public ResponseEntity<?> createExpenseTracker(@Valid @RequestBody JsonNode expenseTrackerRequest) throws ResourceNotCreatedException {

//        SecurityContext context = SecurityContextHolder.getContext();
//        UserDetailsImpl userDetails = (UserDetailsImpl)context.getAuthentication().getPrincipal();
//        expenseTrackerRequest.setCreatedBy(userDetails.getUsername());
//        expenseTrackerRequest.setUpdatedBy(userDetails.getUsername());

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(expenseTrackerRequest.get("mainCategory").asLong());

        if(mainCategoryOpt.isPresent()) {

            // read currently logged-in user into UserService
            this.userService.setSecurityContext();

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            ExpenseTracker newExpenseTracker = new ExpenseTracker();

            newExpenseTracker.setName(expenseTrackerRequest.get("name").asText());
            newExpenseTracker.setUser(userDetails.getUser());
            newExpenseTracker.setMainCategory(mainCategoryOpt.get());

            newExpenseTracker.setCreatedBy(userDetails.getUsername());
            newExpenseTracker.setUpdatedBy(userDetails.getUsername());


            ExpenseTracker expenseTracker = this.expenseTrackerService.save(newExpenseTracker);


            return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(expenseTracker), HttpStatus.CREATED);
        }
        String errorMessage = String.format("Main category with name (%s),has not been created!", expenseTrackerRequest.get("name").asText());
        return this.errorHandler.handleResourceNotCreatedError(errorMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseTrackerResponseDto> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                .createWith(String.format("The requested id (%d) has not been found!",id));

        resourceNFException.setId(id);

        ExpenseTracker expenseTracker = this.expenseTrackerService.findById(id).orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(expenseTracker), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseTrackerResponseDto>>getAllExpenseTrackers() {

        List<ExpenseTracker> expenseTrackerCollection = this.expenseTrackerService.findAll();
        return new ResponseEntity<>(expenseTrackerCollection
                .stream()
                .map(expenseTrackerMapper::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) throws CustomException {

        try{
            this.expenseTrackerService.deleteById(id);
        }catch (EmptyResultDataAccessException ex){
            ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                    .createWith(String.format("The requested id (%d) has not been found!",id));
            resourceNFException.setId(id);
            throw resourceNFException;
        }

        //TODO successful feedback
        return new ResponseEntity<>("id", HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<ExpenseTrackerResponseDto> updateMainCategory(@Valid @RequestBody ExpenseTracker expenseTrackerRequest) throws ResourceNotCreatedException, ResourceNotUpdatedException {

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl userDetails = (UserDetailsImpl)context.getAuthentication().getPrincipal();
        expenseTrackerRequest.setUpdatedBy(userDetails.getUsername());

        ExpenseTracker expenseTracker = this.expenseTrackerService.updateMainCategory(expenseTrackerRequest);


        return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(expenseTracker), HttpStatus.OK);
    }
}
