package com.codercampus.api.service.domain;

import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.*;
import com.codercampus.api.model.compositeId.ExpenseItemId;
import com.codercampus.api.repository.resource.ExpenseRepo;
import com.codercampus.api.repository.resource.ItemRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ExpenseService {

    private final UserService userService;
    private final ExpenseTrackerService expenseTrackerService;
    private final ExpenseRepo expenseRepo;
    private final ItemRepo itemRepo;
    private final ExpenseAddressService expenseAddressService;
    private final ExpensePaymentTypeService expensePaymentTypeService;
    private final ExpenseTypeService expenseTypeService;
    private final ExpenseItemService expenseItemService;
    private final ObjectMapper objectMapper;
    public ExpenseService(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseRepo expenseRepo,
            ExpenseAddressService expenseAddressService,
            ExpenseTypeService expenseTypeService,
            ExpensePaymentTypeService expensePaymentTypeService,
            ItemRepo itemRepo,
            ObjectMapper objectMapper,
            ExpenseItemService expenseItemService
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseRepo = expenseRepo;
        this.expenseAddressService = expenseAddressService;
        this.expensePaymentTypeService = expensePaymentTypeService;
        this.expenseTypeService = expenseTypeService;
        this.itemRepo = itemRepo;
        this.objectMapper = objectMapper;
        this.expenseItemService = expenseItemService;
    }

    /**
     *
     * @param expense
     * @return
     */
    public Expense save(Expense expense){
        return this.expenseRepo.save(expense);
    }

    /**
     *
     * @param expense
     * @return
     */
    @Transactional
    public Optional<Expense> createIfNotExists(
            Expense expense,
            ExpenseTracker expenseTracker,
            JsonNode expenseItemRowsNode
    ){

        if(this.expenseRepo.existsByExpenseName(expense.getExpenseName())){
            return Optional.empty();
        }
        //set to list
        List<ExpenseItem> expenseItemRows = List.copyOf(expense.getExpenseItems());
        expense.getExpenseItems().clear();
//        Optional<ExpenseTracker> expenseTrackerOpt = this.expenseTrackerService.findById(expenseTrackerId);
//        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressService.findById(expenseAddressId);
//        Optional<ExpenseType> expenseTypeOpt = this.expenseTypeService.findById(expenseTypeId);
//        Optional<ExpensePaymentType> expensePaymentTypeOpt = this.expensePaymentTypeService.findById(expensePaymentTypeId);
        Expense savedExpense = this.expenseRepo.save(expense);
        savedExpense.setExpenseTracker(expenseTracker);
//        List<ExpenseItem> expenseItemList= new ArrayList<>();
        UserDetailsImpl userDetails = this.userService.getUserDetails();

        // save expense items if there are any
        if (expenseItemRows.size() > 0){
            expenseItemRows.forEach((expenseItemRow) ->{
                //                    Long rowId = expenseItemRow.get("rowId").asLong();

//                    ExpenseItem expenseItem = this.objectMapper.treeToValue(expenseItemRow,ExpenseItem.class);
//                    Item itemEntity = this.objectMapper.treeToValue(expenseItemRow.get("item"),Item.class);
//                    Item itemEntity = expenseItemRow.getItem();

//                    ExpenseItem expenseItem = new ExpenseItem(savedExpense,expenseItemRow.getItem(),expenseItemRow.getId().getRowId());
                expenseItemRow.setId(new ExpenseItemId(savedExpense.getId(),expenseItemRow.getItem().getId(),expenseItemRow.getId().getRowId()));
                expenseItemRow.setExpense(savedExpense);
//                    expenseItem.setId(expenseItemId);
//                    expenseItem.setExpense(savedExpense);
//                    expenseItem.setItem(itemEntity);
//                    expenseItemRow.setAmount(new BigDecimal(expenseItemRow.get("amount").asLong()));
//                    expenseItemRow.setUnitPrice(new BigDecimal(expenseItemRow.get("unitPrice").asLong()));
//                    expenseItemRow.setPrice(new BigDecimal(expenseItemRow.get("price").asLong()));
//                    expenseItemRow.setItemCategory(this.objectMapper.treeToValue(expenseItemRow.get("itemCategory"),ItemCategory.class));
//                    expenseItemRow.setUnitType(this.objectMapper.treeToValue(expenseItemRow.get("unitType"),UnitType.class));


                expenseItemRow.setCreatedBy(userDetails.getUsername());
                expenseItemRow.setUpdatedBy(userDetails.getUsername());
//                    expenseItemList.add(expenseItem);
                //                try {
//                    ExpenseItem expenseItemEntity = this.objectMapper.treeToValue(expenseItem,ExpenseItem.class);
//                    ExpenseItemId expenseItemId = new ExpenseItemId(savedExpense.getId(),expenseItemEntity.getId(),rowId);
//                    expenseItem.setId(expenseItemId);
//                    expenseItem.setExpense(savedExpense);
//                    expenseItem.setItem(itemEntity);
//                    expenseItem.setCreatedBy(userDetails.getUsername());
//                    expenseItem.setUpdatedBy(userDetails.getUsername());
//                    expenseItemList.add(expenseItem);
//                } catch (JsonProcessingException e) {
//                    e.printStackTrace();
//                }

            });
        }




//        expenseItems.forEach((expenseItem) ->{
//            Item item = expenseItem.getItem();
//            Long rowId = expenseItem.getId();
//                ExpenseItem expenseItemEntity = new ExpenseItem(expense,expenseItem.getItem(),item.);
//            });
//        if(expenseTrackerOpt.isPresent() && expenseAddressOpt.isPresent() && expenseTypeOpt.isPresent() && expensePaymentTypeOpt.isPresent()) {

//            ExpenseTracker expenseTracker = expenseTrackerOpt.get();
//            ExpenseAddress expenseAddress = expenseAddressOpt.get();
//            ExpenseType expenseType = expenseTypeOpt.get();
//            ExpensePaymentType expensePaymentType = expensePaymentTypeOpt.get();


//            expense.getExpenseTracker().addExpense(savedExpense);
//            savedExpense.setExpenseTracker(expense.getExpenseTracker());

//            expenseAddress.addExpense(expense);
//            expense.setExpenseAddress(expenseAddress);
//
//            expenseType.addExpense(expense);
//            expense.setExpenseType(expenseType);
//
//            expensePaymentType.addExpense(expense);
//            expense.setExpensePaymentType(expensePaymentType);

            savedExpense.setCreatedBy(userDetails.getUsername());
            savedExpense.setUpdatedBy(userDetails.getUsername());

        this.expenseItemService.saveAll(expenseItemRows);

        return Optional.of(savedExpense);
        }
//        return Optional.of

//    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.expenseRepo.existsByExpenseName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Expense> findById(Long id){
        return this.expenseRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<Expense> findAll(){
        return this.expenseRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Expense deleteById(Long id) throws ResourceNotFoundException {

        Optional<Expense> expenseOpt = this.expenseRepo.findById(id);

        if(expenseOpt.isPresent()){

            Expense expense = expenseOpt.get();

            expense.getExpenseTracker().removeExpense(expense);

            for(ExpenseItem expenseItem : expense.getExpenseItems()){
                Item item = expenseItem.getItem();
                expenseItem.getExpense().removeItem(item);
                item.removeExpense(expense);
                this.itemRepo.save(item);
            }
            this.expenseRepo.deleteById(id);
            return expense;
        } else{
            throw ResourceNotFoundException.createWith("Expense could not be found!");
        }
    }

    /**
     *
     * @param expenseTracker
     * @param sourceExpense
     * @return
     */
    public Expense update(Expense sourceExpense, ExpenseTracker expenseTracker ) throws ResourceNotFoundException {

        sourceExpense.setExpenseTracker(expenseTracker);
        Optional<Expense> targetExpenseOpt = this.expenseRepo.findById(sourceExpense.getId());
        if(targetExpenseOpt.isPresent()){

            Expense targetExpense = targetExpenseOpt.get();

            targetExpense.setExpenseName(sourceExpense.getExpenseName());
            targetExpense.setExpenseEmail(sourceExpense.getExpenseEmail());
            targetExpense.setExpensePhone(sourceExpense.getExpensePhone());
            targetExpense.setExpenseMobile(sourceExpense.getExpenseMobile());

            targetExpense.setExpenseType(sourceExpense.getExpenseType());
            targetExpense.setExpenseAddress(sourceExpense.getExpenseAddress());
            targetExpense.setExpensePaymentType(sourceExpense.getExpensePaymentType());
            targetExpense.setExpenseComment(sourceExpense.getExpenseComment());
            targetExpense.getExpenseItems().clear();
//        targetExpense.setExpenseItems(sourceExpense.getExpenseItems());
            sourceExpense.getExpenseItems().forEach(sourceExpenseItem -> {
                sourceExpenseItem.setExpense(targetExpense);
                // if the ExpenseItem is new
                if(sourceExpenseItem.getId().getExpenseId() == null && sourceExpenseItem.getId().getItemId() == null){
                    sourceExpenseItem.getId().setExpenseId(sourceExpense.getId());
                    sourceExpenseItem.getId().setItemId(sourceExpenseItem.getItem().getId());
                }
                targetExpense.getExpenseItems().add(sourceExpenseItem);
            });
//        expense.setExpenseAddress(expenseAddress);
//        expense.setExpenseType(expenseType);
//        expense.setExpensePaymentType(expensePaymentType);

            return this.save(targetExpense);
        }
        throw ResourceNotFoundException.createWith("Expense could not be found!");

    }
}
