package com.codercampus.api.controller.domain;

import com.codercampus.api.error.GlobalErrorHandlerService;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.payload.mapper.ExpenseAddressMapper;
import com.codercampus.api.payload.mapper.ExpenseTrackerMapper;
import com.codercampus.api.payload.response.responsedto.ExpenseAddressResponseDto;
import com.codercampus.api.service.UserService;
import com.codercampus.api.service.domain.ExpenseAddressService;
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
@RequestMapping("/api/expense-address")
@Validated
public class ExpenseAddressController {

    private final UserService userService;
    private final ExpenseAddressMapper expenseAddressMapper;
    private final ExpenseTrackerMapper expenseTrackerMapper;
    private final ExpenseAddressService expenseAddressService;
    private final ExpenseTrackerService expenseTrackerService;
    private final GlobalErrorHandlerService errorHandler;
    private final ObjectMapper objectMapper;


    public ExpenseAddressController(
            UserService userService,
            ExpenseAddressService expenseAddressService,
            ExpenseAddressMapper expenseAddressMapper,
            ExpenseTrackerService expenseTrackerService,
            GlobalErrorHandlerService globalErrorHandlerService,
            ObjectMapper objectMapper,
            ExpenseTrackerMapper expenseTrackerMapperMapper
    ) {
        this.userService = userService;
        this.expenseAddressService = expenseAddressService;
        this.expenseAddressMapper = expenseAddressMapper;
        this.errorHandler = globalErrorHandlerService;
        this.objectMapper = objectMapper;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseTrackerMapper = expenseTrackerMapperMapper;
    }

    /**
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<ExpenseAddressResponseDto>>getAll() {

        List<ExpenseAddress> expenseAddressCollection = this.expenseAddressService.findAll();
        return new ResponseEntity<>(expenseAddressCollection
                .stream()
                .map(expenseAddressMapper::toResponseDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     * @throws NumberFormatException
     * @throws ResourceNotFoundException
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseAddressResponseDto> findById(@PathVariable("id") Long id) throws NumberFormatException, ResourceNotFoundException {

        ResourceNotFoundException resourceNFException =  ResourceNotFoundException
                .createWith(String.format("The requested id (%d) has not been found!",id));

        resourceNFException.setId(id);

        ExpenseAddress expenseAddress = this.expenseAddressService.findById(id).orElseThrow(() -> resourceNFException);

        return new ResponseEntity<>(this.expenseAddressMapper.toResponseDto(expenseAddress), HttpStatus.OK);
    }

    /**
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody JsonNode request) throws JsonProcessingException {

        ExpenseAddress expenseAddress = this.objectMapper.treeToValue(request,ExpenseAddress.class);

        Long expenseId = request.get("expenseId").asLong();

        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.createIfNotExists(expenseAddress,expenseId);

            if(expenseAddressOpt.isPresent()){
                return new ResponseEntity<>(this.expenseAddressMapper.toResponseDto(expenseAddressOpt.get()), HttpStatus.CREATED);

            }
            //TODO appropriate error
            return this.errorHandler.handleResourceAlreadyExistError(request.get("postCode").asText(),expenseAddress);
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
//        Long expenseTypeId
//        Long expenseAddressId": null,
//        Long expensePaymentTypeId": null,

        ExpenseAddress expenseAddress = this.objectMapper.treeToValue(request,ExpenseAddress.class);

//        if (expenseTrackerOpt.isPresent()){
//            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
            if(this.expenseAddressService.isExists(expenseAddress)){
                //TODO error message
                return this.errorHandler.handleResourceAlreadyExistError(expenseAddress.getPostCode(),expenseAddress);
            }
            ExpenseAddress updatedExpenseAddress = this.expenseAddressService.update(expenseAddress);
            return new ResponseEntity<>(this.expenseAddressMapper.toResponseDto(updatedExpenseAddress), HttpStatus.OK);
//        }
//        return this.errorHandler.handleResourceNotUpdatedError(expense.getName(),expense);

    }

    /**
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id) {

        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.deleteById(id);
        if(expenseAddressOpt.isPresent()){

            //TODO successful feedback
            return new ResponseEntity<>(expenseAddressMapper.toResponseDto(expenseAddressOpt.get()), HttpStatus.OK);

        }
        return this.errorHandler.handleResourceNotFoundError(id.toString(), null);

    }

}
