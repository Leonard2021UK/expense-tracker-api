package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseAddressRepo extends JpaRepository<ExpenseAddress,Long> {

    boolean existsByName(String name);

    boolean existsByAddressLine1AndPostCode(String addressLine1,String postcode);

    @Query(value = "SELECT ea FROM expense_address ea where ea.user.id = :userId")
    List<ExpenseAddress> findAllByUserId(@Param("userId") Long userId);

    Optional<ExpenseAddress> findByIdAndUserId(Long id, Long userId);

    @Modifying
    @Query(value = "DELETE FROM expense_address ea WHERE ea.id = :expenseAddressId AND ea.user.id = :userId")
    void deleteById(@Param("expenseAddressId") Long expenseAddressId, @Param("userId") Long userId);

}
