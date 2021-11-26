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

//    private final UserService userService;
    private final MainCategoryService mainCategoryService;
//    private final GlobalErrorHandlerService errorHandlerService;
    private final ObjectMapper objectMapper;
//    private final MainCategoryMapper mainCategoryMapper;

    public MainCategoryController(
            UserService userService,
            MainCategoryService mainCategoryService,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper,
            MainCategoryMapper mainCategoryMapper
    ) {
        this.userService = userService;
        this.mainCategoryService = mainCategoryService;
        this.errorHandlerService = globalErrorHandlerService;
        this.objectMapper = objectMapper;
        this.mainCategoryMapper = mainCategoryMapper;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<MainCategoryResponseDto>> getAll() {

        List<MainCategory> mainCategoryCollection = this.mainCategoryService.findAll();
        return new ResponseEntity<>(mainCategoryCollection
                .stream()
                .map(this.mainCategoryMapper::toResponseDto)
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
    public ResponseEntity<?> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryService.findById(id);

        if(mainCategoryOpt.isPresent()){
            return new ResponseEntity<>(this.mainCategoryMapper.toResponseDto(mainCategoryOpt.get()), HttpStatus.OK);
        }

        return this.errorHandlerService.handleResourceNotFoundError(id.toString(), null);

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
