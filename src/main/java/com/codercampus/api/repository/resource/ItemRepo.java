package com.codercampus.api.repository.resource;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends JpaRepository<Item,Long> {
    boolean existsByName(String name);
}
