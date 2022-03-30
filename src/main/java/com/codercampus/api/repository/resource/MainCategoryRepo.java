package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.model.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MainCategoryRepo extends JpaRepository<MainCategory,Long> {

    boolean existsByName(String name);

    @Query(value = "SELECT mc FROM main_category mc where mc.user.id = :userId")
    List<MainCategory> findAllByUserId(@Param("userId") Long userId);

    @Query(value = "SELECT mc FROM main_category mc where mc.id = :mainCategoryId AND mc.user.id = :userId")
    Optional<MainCategory> findByIdAndUserId(@Param("mainCategoryId") Long id, @Param("userId") Long userId);

    @Modifying
    @Query(value = "DELETE FROM main_category mc where mc.id = :mainCategoryId and mc.user.id = :userId")
    void deleteByIdAndUserId(@Param("mainCategoryId") Long id, @Param("userId") Long userId);
}
