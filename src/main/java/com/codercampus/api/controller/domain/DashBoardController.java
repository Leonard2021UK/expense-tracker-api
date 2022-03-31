package com.codercampus.api.controller.domain;

import com.codercampus.api.model.views.ExpenseTrackerView;
import com.codercampus.api.model.views.ItemCategoryView;
import com.codercampus.api.repository.views.ItemCategorySumViewRepo;
import com.codercampus.api.service.view.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/dashboard/expense-tracker")
public class DashBoardController {

    DashboardService dashboardService;

    public DashBoardController(DashboardService dashboardService){
        this.dashboardService = dashboardService;
    }

    @GetMapping("/item/itemCategory")
    public ResponseEntity<?> findAllItemCategorySumView(){
        List<ItemCategoryView> sum =  this.dashboardService.findAllItemCategoryByPeriod();
        return new ResponseEntity<>((sum), HttpStatus.OK);
    }

    @GetMapping("/sum")
    public ResponseEntity<?> findAllExpenseTrackerView(){
        List<ExpenseTrackerView> sum =  this.dashboardService.findAll();
        return new ResponseEntity<>((sum), HttpStatus.OK);
    }


}
