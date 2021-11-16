package com.codercampus.api.controller.domain;

import com.codercampus.api.exception.CustomException;
import com.codercampus.api.exception.ResourceNotCreatedException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.exception.ResourceNotUpdatedException;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.mapper.MainCategoryMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseTrackerResponseDto;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.resource.ExpenseTrackerService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/expense-tracker")
@Validated
public class ExpenseTrackerController {

    private final UserService userService;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseTrackerService expenseTrackerService;

    public ExpenseTrackerController(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseTrackerMapper expenseTrackerMapper
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapper;
    }

    @PostMapping
    public ResponseEntity<ExpenseTrackerResponseDto> createExpenseTracker(@Valid @RequestBody ExpenseTracker expenseTrackerRequest) throws ResourceNotCreatedException {

//        SecurityContext context = SecurityContextHolder.getContext();
//        UserDetailsImpl userDetails = (UserDetailsImpl)context.getAuthentication().getPrincipal();
//        expenseTrackerRequest.setCreatedBy(userDetails.getUsername());
//        expenseTrackerRequest.setUpdatedBy(userDetails.getUsername());

        // read currently logged in user into UserService
        this.userService.setSecurityContext();

        UserDetailsImpl userDetails = this.userService.getUserDetails();

        expenseTrackerRequest.setUser(userDetails.getUser());
        expenseTrackerRequest.setCreatedBy(userDetails.getUsername());
        expenseTrackerRequest.setUpdatedBy(userDetails.getUsername());

        ResourceNotCreatedException resourceNCException = ResourceNotCreatedException
                .createWith(String.format("Main category with name (%s),has not been created!",expenseTrackerRequest.getName()));

        ExpenseTracker expenseTracker = this.expenseTrackerService.save(expenseTrackerRequest)
                .orElseThrow(() -> resourceNCException);

        return new ResponseEntity<>(this.expenseTrackerMapper.toResponseDto(expenseTracker), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseTracker> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                .createWith(String.format("The requested id (%d) has not been found!",id));

        resourceNFException.setId(id);

        ExpenseTracker expenseTracker = this.expenseTrackerService.findById(id).orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(expenseTracker, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseTracker>>getAllMainCategory() {

        List<ExpenseTracker> expenseTrackers = this.expenseTrackerService.findAll();

        return new ResponseEntity<>(expenseTrackers, HttpStatus.OK);
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
    public ResponseEntity<ExpenseTracker> updateMainCategory(@Valid @RequestBody ExpenseTracker mainCategoryRequest) throws ResourceNotCreatedException, ResourceNotUpdatedException {

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl userDetails = (UserDetailsImpl)context.getAuthentication().getPrincipal();
        mainCategoryRequest.setUpdatedBy(userDetails.getUsername());

        ResourceNotUpdatedException resourceNUException = ResourceNotUpdatedException
                .createWith(String.format("Main category with name (%s),was not updated!",mainCategoryRequest.getName()));

        ExpenseTracker expenseTrackers = this.expenseTrackerService.updateMainCategory(mainCategoryRequest)
                .orElseThrow(() -> resourceNUException);

        return new ResponseEntity<>(expenseTrackers, HttpStatus.OK);
    }
}
