package com.codercampus.api.service.resource;

import com.codercampus.api.model.MainCategory;
import com.codercampus.api.repository.resource.MainCategoryRepo;
import org.springframework.stereotype.Service;

@Service
public class MainCategoryService {

    private final MainCategoryRepo mainCategoryRepo;

    public MainCategoryService(MainCategoryRepo mainCategoryRepo) {
        this.mainCategoryRepo = mainCategoryRepo;
    }

    public MainCategory createMainCategory(MainCategory mainCategory){
        return this.mainCategoryRepo.save(mainCategory);
    };
}
