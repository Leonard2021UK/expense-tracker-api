package com.codercampus.api.service.resource;

import com.codercampus.api.model.MainCategory;
import com.codercampus.api.repository.resource.MainCategoryRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MainCategoryService {

    private final MainCategoryRepo mainCategoryRepo;

    public MainCategoryService(MainCategoryRepo mainCategoryRepo) {
        this.mainCategoryRepo = mainCategoryRepo;
    }

    public Optional<MainCategory> createMainCategory(MainCategory mainCategory){
        return Optional.of(this.mainCategoryRepo.save(mainCategory));
    }

    public Optional<MainCategory> findById(Long id){
        return this.mainCategoryRepo.findById(id);
    }

}
