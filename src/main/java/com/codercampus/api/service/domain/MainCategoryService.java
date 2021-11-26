package com.codercampus.api.service.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.model.MainCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.payload.mapper.MainCategoryMapper;
import com.codercampus.api.payload.response.responsedto.MainCategoryResponseDto;
import com.codercampus.api.repository.resource.MainCategoryRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MainCategoryService {

    private final UserService userService;
    private final MainCategoryRepo mainCategoryRepo;
    private final MainCategoryMapper mainCategoryMapper;
    private final GlobalErrorHandlerService errorHandlerService;

    /**
     *
     * @param userService
     * @param mainCategoryRepo
     */
    public MainCategoryService(
            UserService userService,
            MainCategoryRepo mainCategoryRepo,
            MainCategoryMapper mainCategoryMapper,
            GlobalErrorHandlerService globalErrorHandlerService
    ) {
        this.userService = userService;
        this.mainCategoryRepo = mainCategoryRepo;
        this.mainCategoryMapper = mainCategoryMapper;
        this.errorHandlerService = globalErrorHandlerService;

    }

    /**
     *
     * @param mainCategory
     * @return
     */
    public MainCategory save(MainCategory mainCategory){
        return this.mainCategoryRepo.save(mainCategory);
    }

    /**
     *
     * @param mainCategory
     * @return
     */
    public ResponseEntity<?> createIfNotExists(MainCategory mainCategory){
        if(this.mainCategoryRepo.existsByName(mainCategory.getName())){
            return this.errorHandlerService.handleResourceAlreadyExistError(mainCategory.getName(),mainCategory);
        }else{

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            mainCategory.setCreatedBy(userDetails.getUsername());
            mainCategory.setUpdatedBy(userDetails.getUsername());

            userDetails.getUser().addMainCategory(mainCategory);
            return new ResponseEntity<>(this.mainCategoryMapper.toResponseDto(this.mainCategoryRepo.save(mainCategory)), HttpStatus.CREATED);

        }

    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.mainCategoryRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<?> findById(Long id){

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryRepo.findById(id);
        if(mainCategoryOpt.isPresent()){
            return new ResponseEntity<>(this.mainCategoryMapper.toResponseDto(mainCategoryOpt.get()), HttpStatus.OK);
        }

        return this.errorHandlerService.handleResourceNotFoundError(id.toString(), null);
    }

    /**
     *
     * @return
     */
    public ResponseEntity<List<MainCategoryResponseDto>> findAll(){
        return new ResponseEntity<>(this.mainCategoryRepo.findAll()
                .stream()
                .map(this.mainCategoryMapper::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<?> deleteById(Long id){

        Optional<MainCategory> mainCategoryOpt = this.mainCategoryRepo.findById(id);

        if(mainCategoryOpt.isPresent()){
            MainCategory mainCategory = mainCategoryOpt.get();

            mainCategory.getUser().getMainCategories().remove(mainCategory);

            return new ResponseEntity<>(this.mainCategoryMapper.toResponseDto(this.save(mainCategory)), HttpStatus.OK);

        }
        return this.errorHandlerService.handleResourceNotFoundError(id.toString(),null);
    }

    /**
     *
     * @param mainCategory
     * @return
     */
    public ResponseEntity<?> update(MainCategory mainCategory){

        // if the new main category name exist then return a corresponding error
        if(this.isExists(mainCategory.getName())){
            return this.errorHandlerService.handleResourceAlreadyExistError(mainCategory.getName(),mainCategory);
        }

        User user = this.userService.getUserDetails().getUser();

        //TODO examine the way how it could be extracted from UserDetailsImpl
        mainCategory.setUser(user);

        UserDetailsImpl userDetails = this.userService.getUserDetails();
        mainCategory.setUpdatedBy(userDetails.getUsername());
        return new ResponseEntity<>(this.mainCategoryMapper.toResponseDto(this.save(mainCategory)), HttpStatus.OK);

    }
}
