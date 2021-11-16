package com.codercampus.api.repository.resource;

import com.codercampus.api.model.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainCategoryRepo extends JpaRepository<MainCategory,Long> {

    public boolean existsByName(String name);
}
