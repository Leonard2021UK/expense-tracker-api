package com.codercampus.api.controller.domain;

import com.codercampus.api.exception.CustomException;
import com.codercampus.api.exception.ResourceNotCreatedException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import com.codercampus.api.payload.mapper.MainCategoryMapper;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.resource.MainCategoryService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/main-category")
@Validated
public class MainCategoryController {

    private final UserService userService;
    private final MainCategoryService mainCategoryService;
    private final MainCategoryMapper mainCategoryMapper;

    public MainCategoryController(
            UserService userService,
            MainCategoryService mainCategoryService,
            MainCategoryMapper mainCategoryMapper
    ) {
        this.userService = userService;
        this.mainCategoryService = mainCategoryService;
        this.mainCategoryMapper = mainCategoryMapper;
    }

    @PostMapping
    public ResponseEntity<MainCategoryResponseDto> createMainCategory(@Valid @RequestBody MainCategory mainCategoryRequest) throws ResourceNotCreatedException {

        // read currently logged in user into UserService
        this.userService.setSecurityContext();

        UserDetailsImpl userDetails = this.userService.getUserDetails();

        mainCategoryRequest.setUser(userDetails.getUser());
        mainCategoryRequest.setCreatedBy(userDetails.getUsername());
        mainCategoryRequest.setUpdatedBy(userDetails.getUsername());

        userDetails.getUser().addMainCategory(mainCategoryRequest);

        ResourceNotCreatedException resourceNFException = ResourceNotCreatedException
                .createWith(String.format("Main category with name (%s),has not been created!",mainCategoryRequest.getName()));

        MainCategory mainCategory = this.mainCategoryService.save(mainCategoryRequest)
                .orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(mainCategoryMapper.toResponseDto(mainCategory), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MainCategoryResponseDto> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {
        ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                .createWith(String.format("The requested id (%d) has not been found!",id));
        resourceNFException.setId(id);

        MainCategory mainCategory = this.mainCategoryService.findById(id).orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(mainCategoryMapper.toResponseDto(mainCategory), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MainCategoryResponseDto>> getAllMainCategory() {

        List<MainCategory> mainCategoryCollection = this.mainCategoryService.findAll();

        return new ResponseEntity<>(mainCategoryCollection
                .stream()
                .map(mainCategoryMapper::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) throws CustomException {

        try{
            this.mainCategoryService.deleteById(id);
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
    public ResponseEntity<MainCategoryResponseDto> updateMainCategory(@Valid @RequestBody MainCategory mainCategoryRequest) throws ResourceNotCreatedException {

        // read currently logged in user into UserService
        this.userService.setSecurityContext();

        UserDetailsImpl userDetails = this.userService.getUserDetails();
        mainCategoryRequest.setUpdatedBy(userDetails.getUsername());

        ResourceNotCreatedException resourceNFException = ResourceNotCreatedException
                .createWith(String.format("Main category with name (%s),was not updated!",mainCategoryRequest.getName()));

        MainCategory mainCategory = this.mainCategoryService.updateMainCategory(mainCategoryRequest)
                .orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(this.mainCategoryMapper.toResponseDto(mainCategory), HttpStatus.OK);
    }
}
