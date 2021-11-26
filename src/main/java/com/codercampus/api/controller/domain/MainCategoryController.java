package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import com.codercampus.api.payload.mapper.MainCategoryMapper;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.MainCategoryService;
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
@RequestMapping("/api/main-category")
@Validated
public class MainCategoryController {

    private final MainCategoryService mainCategoryService;
    private final ObjectMapper objectMapper;

    public MainCategoryController(
            MainCategoryService mainCategoryService,
            ObjectMapper objectMapper
    ) {
        this.mainCategoryService = mainCategoryService;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<MainCategoryResponseDto>> finAll() {

        return this.mainCategoryService.findAll();

    }

    /**
     *
     * @param id
     * @return
     * @throws NumberFormatException
     * @throws ResourceNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        return this.mainCategoryService.findById(id);

    }

    /**
     *
     * @param mainCategoryRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody MainCategory mainCategoryRequest) {

        return this.mainCategoryService.createIfNotExists(mainCategoryRequest);

    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PatchMapping
    public ResponseEntity<?> update(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

        MainCategory newMainCategory = this.objectMapper.treeToValue(request,MainCategory.class);

        return this.mainCategoryService.update(newMainCategory);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {

        return this.mainCategoryService.deleteById(id);

    }


}
