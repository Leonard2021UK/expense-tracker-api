package com.codercampus.api.controller.utility;

import com.codercampus.api.model.*;
import com.codercampus.api.payload.response.responsedto.AppInitResponse;
import com.codercampus.api.service.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequestMapping("/api/app-init")
@CrossOrigin(origins = {"https://127.0.0.1:3000","https://expense-tracker-client-2022.herokuapp.com", "https://localhost:3000"}, maxAge = 3600)
@RestController
public class AppInitController {

    private final ExpenseTrackerService expenseTrackerService;
    private final ExpenseTypeService expenseTypeService;
    private final ExpenseAddressService expenseAddressService;
    private final ExpensePaymentTypeService expensePaymentTypeService;
    private final UnitTypeService unitTypeService;

    public AppInitController(
             ExpenseTrackerService expenseTrackerService,
             ExpenseTypeService expenseTypeService,
             ExpenseAddressService expenseAddressService,
             ExpensePaymentTypeService expensePaymentTypeService,
             UnitTypeService unitTypeService
    ) {
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTypeService = expenseTypeService;
        this.expenseAddressService = expenseAddressService;
        this.expensePaymentTypeService = expensePaymentTypeService;
        this.unitTypeService = unitTypeService;
    }

    @GetMapping
    public ResponseEntity<?> initialize(){

        List<ExpenseTracker> expenseTrackers = this.expenseTrackerService.findAll();
        List<ExpenseType> expenseTypes = this.expenseTypeService.findAll();
        List<ExpenseAddress> expenseAddresses = this.expenseAddressService.findAll();
        List<ExpensePaymentType> expensePaymentTypes = this.expensePaymentTypeService.findAll();
        List<UnitType> unitTypes = this.unitTypeService.findAllNoneArchived();

        AppInitResponse appInitResponse = new AppInitResponse.AppInitResponseBuilder(expenseTrackers)
                .expenseTypes(expenseTypes)
                .expenseAddresses(expenseAddresses)
                .expensePaymentTypes(expensePaymentTypes)
                .unitTypes(unitTypes)
                .build();
        return new ResponseEntity<>(appInitResponse, HttpStatus.OK);
    }

}
