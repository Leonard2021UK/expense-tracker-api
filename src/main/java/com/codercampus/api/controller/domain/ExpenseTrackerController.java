package com.codercampus.api.controller.domain;

import com.codercampus.api.payload.dto.ExpenseTrackerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expense-tracker")
public class ExpenseTrackerController {

    @PostMapping("/")
    public ResponseEntity<ExpenseTrackerDto> createExpenseTracker(){
        return null;
    }
}
