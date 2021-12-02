package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.model.UnitType;
import com.codercampus.api.service.domain.UnitTypeService;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.mapper.UnitTypeMapper;
import com.codercampus.api.payload.response.responsedto.UnitTypeResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.ExpenseService;
import com.codercampus.api.service.domain.ExpenseTrackerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/unit-type")
@Validated
public class UnitTypeController {

    private final UserService userService;
    private final UnitTypeMapper unitTypeMapper;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final UnitTypeService unitTypeService;
    private final ExpenseTrackerService expenseTrackerService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;
    private final ExpenseService expenseService;

    public UnitTypeController(
            UserService userService,
            UnitTypeService unitTypeService,
            UnitTypeMapper unitTypeMapper,
            ExpenseTrackerService expenseTrackerService,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper,
            ExpenseTrackerMapper expenseTrackerMapperMapper,
            ExpenseService expenseService

    ) {
        this.userService = userService;
        this.unitTypeService = unitTypeService;
        this.unitTypeMapper = unitTypeMapper;
        this.errorHandler = globalErrorHandlerService;
        this.objectMapper = objectMapper;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapperMapper;
        this.expenseService = expenseService;

    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<UnitTypeResponseDto>>getAll() {

        List<UnitType> unitTypeCollection = this.unitTypeService.findAll();
        return new ResponseEntity<>(unitTypeCollection
                .stream()
                .map(unitTypeMapper::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     * @throws NumberFormatException
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {


        Optional<UnitType> unitType = this.unitTypeService.findById(id);

        if (unitType.isPresent()){
            return new ResponseEntity<>(this.unitTypeMapper.toResponseDto(unitType.get()), HttpStatus.OK);
        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(),null);

    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

        UnitType unitType = this.objectMapper.treeToValue(request,UnitType.class);
//
//        Long expenseId = request.get("expenseId").asLong();

//        Optional<Expense> expenseOpt = this.expenseService.findById(expenseId);

//        if(expenseOpt.isPresent()){
//            Expense expense = expenseOpt.get();
        Optional<UnitType> unitTypeOpt = this.unitTypeService.createIfNotExists(unitType);

        if(unitTypeOpt.isPresent()){
            return new ResponseEntity<>(this.unitTypeMapper.toResponseDto(unitTypeOpt.get()), HttpStatus.CREATED);

        }

        //TODO appropriate error
        return this.errorHandler.handleResourceAlreadyExistError(request.get("name").asText(),unitType);
    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PatchMapping
    public ResponseEntity<?> update(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

//        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(request.get("expenseTrackerId").asLong());
//        Long expenseTrackerId = request.get("expenseTrackerId")
//        Long unitTypeId
//        Long unitTypeId": null,
//        Long expensePaymentTypeId": null,

        UnitType unitType = this.objectMapper.treeToValue(request,UnitType.class);

//        if (expenseTrackerOpt.isPresent()){
//            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
//            if(this.unitTypeService.isExists(unitType.getName())){
//                //TODO error message
//                return this.errorHandler.handleResourceAlreadyExistError(unitType.getPostCode(),unitType);
//            }
            UnitType updatedUnitType = this.unitTypeService.update(unitType);
            return new ResponseEntity<>(this.unitTypeMapper.toResponseDto(updatedUnitType), HttpStatus.OK);
//        }
//        return this.errorHandler.handleResourceNotUpdatedError(expense.getName(),expense);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> archiveById(@PathVariable("id") Long id) {

        Optional<UnitType> unitType = this.unitTypeService.deleteById(id);

        if(unitType.isPresent()){

            //TODO successful feedback
            return new ResponseEntity<>(unitTypeMapper.toResponseDto(unitType.get()), HttpStatus.OK);

        }

        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
