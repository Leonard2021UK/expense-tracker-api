package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ItemCategory;
import com.codercampus.api.model.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCategoryRepo extends JpaRepository<ItemCategory,Long> {
    boolean existsByName(String name);
}
