package com.codercampus.api.controller.domain;

import com.codercampus.api.model.MainCategory;
import com.codercampus.api.payload.dto.MainCategoryDto;
import com.codercampus.api.service.resource.MainCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/main-category")
public class MainCategoryController {

    private final MainCategoryService mainCategoryService;

    public MainCategoryController(MainCategoryService mainCategoryService) {
        this.mainCategoryService = mainCategoryService;
    }

    @PostMapping("/")
    public ResponseEntity<MainCategory> createMainCategory(@RequestBody MainCategory request){
        MainCategory savedMainCategory = this.mainCategoryService.createMainCategory(request);
        return ResponseEntity.ok().body(savedMainCategory);
    }
}
