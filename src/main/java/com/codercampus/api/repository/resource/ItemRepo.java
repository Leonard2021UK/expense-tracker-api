package com.codercampus.api.repository.resource;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseItem;
import com.codercampus.api.model.Item;
import com.codercampus.api.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepo extends JpaRepository<Item,Long> {
    boolean existsByName(String name);


    @Query(value = "SELECT It FROM item It WHERE It.isArchived = false AND It.user.id = :userId")
    List<Item> findAllNoneArchived(Long userId);

    @Modifying
    @Query(value = "DELETE FROM item It WHERE It.id = :itemId AND It.user.id = :userId")
    void deleteById(@Param("itemId") Long itemId, @Param("userId") Long userId);

    @Override
    <S extends Item> List<S> saveAll(Iterable<S> entities);
}
