package com.codercampus.api.repository.resource;

import com.codercampus.api.model.ExpenseAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseAddressRepo extends JpaRepository<ExpenseAddress,Long> {

    boolean existsByAddressLine1AndPostCode(String addressLine1,String postcode);

}
