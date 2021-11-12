package com.codercampus.api.controller.domain;

import com.codercampus.api.exception.CategoryNotCreatedException;
import com.codercampus.api.exception.CategoryNotFoundException;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.dto.MainCategoryDto;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.resource.MainCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/main-category")
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
        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.createMainCategory(mainCategoryRequest);

        return ResponseEntity.ok().body(mainCategoryOpt.orElseThrow(() -> CategoryNotCreatedException.createWith(mainCategoryRequest.getName())));
    }

}
