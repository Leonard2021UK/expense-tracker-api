package com.codercampus.api.repository.resource;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseItemRepo extends JpaRepository<ExpenseItem,Long> {

    @Override
    <S extends ExpenseItem> List<S> saveAll(Iterable<S> entities);
}
