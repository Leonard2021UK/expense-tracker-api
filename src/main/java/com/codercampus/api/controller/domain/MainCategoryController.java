package com.codercampus.api.controller.domain;

import com.codercampus.api.exception.CategoryNotCreatedException;
import com.codercampus.api.exception.CategoryNotFoundByIdException;
import com.codercampus.api.exception.CategoryNotFoundByNameException;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.resource.MainCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.util.Optional;

@RestController
@RequestMapping("/api/main-category")
@Validated
public class MainCategoryController {

    private final MainCategoryService mainCategoryService;

    public MainCategoryController(MainCategoryService mainCategoryService) {
        this.mainCategoryService = mainCategoryService;
    }

    @PostMapping
    public ResponseEntity<MainCategory> createMainCategory(@Valid @RequestBody MainCategory mainCategoryRequest) throws CategoryNotCreatedException {

        SecurityContext context = SecurityContextHolder.getContext();
        UserDetailsImpl userDetails = (UserDetailsImpl)context.getAuthentication().getPrincipal();
        mainCategoryRequest.setCreatedBy(userDetails.getUsername());
        MainCategory mainCategory = this.mainCategoryService.createMainCategory(mainCategoryRequest)
                .orElseThrow(() -> CategoryNotCreatedException.createWith(mainCategoryRequest.getName()));

        return new ResponseEntity<>(mainCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainCategory> findById(@PathVariable("id") Long id) throws CategoryNotFoundByIdException,NumberFormatException {

//               Long categoryId =  Long.valueOf(id);
               MainCategory mainCategory = this.mainCategoryService.findById(id).orElseThrow(() -> CategoryNotFoundByIdException.createWith(id));

               return new ResponseEntity<>(mainCategory, HttpStatus.CREATED);
    }

//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
//        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//    }

}
