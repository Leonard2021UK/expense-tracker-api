package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ItemCategory;
import com.codercampus.api.model.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemCategoryRepo extends JpaRepository<ItemCategory,Long> {
    boolean existsByName(String name);

//    @Query(value = "SELECT Ic FROM item_category Ic WHERE Ic.user.id = :userId")
    List<ItemCategory> findAllByUserId(Long userId);

//    @Query(value = "SELECT Ic FROM item_category Ic WHERE Ic.id = :itemCategoryId AND Ic.user.id = :userId")
    Optional<ItemCategory> findByIdAndUserId(Long itemCategoryId, Long userId);

    @Modifying
    @Query(value = "DELETE FROM item_category Ic WHERE Ic.id = :itemCategoryId AND Ic.user.id = :userId")
    void deleteById(@Param("itemCategoryId") Long itemCategoryId, @Param("userId") Long userId);

}
