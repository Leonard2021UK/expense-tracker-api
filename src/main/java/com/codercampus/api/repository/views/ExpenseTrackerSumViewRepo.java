package com.codercampus.api.repository.views;

import com.codercampus.api.model.views.ExpenseTrackerView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseTrackerSumViewRepo extends JpaRepository<ExpenseTrackerView, Long> {

//    @Query(value = "SELECT et.id as etid, et.name as name, sum(ei.price) as sum FROM expense_tracker et\n" +
//            "    left JOIN expense e on et.id=e.expenseTracker.id \n" +
//            "    join  expenseItem ei on e.id = ei.expense.id\n" +
//            "    join item i on i.id = ei.item.id where et.user.id= 2 group by et.id")
    @Query(value = "SELECT ev FROM etsumview ev WHERE ev.uid = :userId ")
    List<ExpenseTrackerView> getExpenseTrackerSumByUserId(Long userId);
}
