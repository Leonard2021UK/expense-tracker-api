package com.codercampus.api.service.domain;

import com.codercampus.api.model.*;
import com.codercampus.api.repository.resource.ExpenseRepo;
import com.codercampus.api.repository.resource.ItemRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final UserService userService;
    private final ExpenseService expenseService;
    private final ItemRepo itemRepo;
    private final UnitTypeService unitTypeService;
    private final ItemCategoryService itemCategoryService;
    private final ExpenseAddressService expenseAddressService;
    private final ExpensePaymentTypeService expensePaymentTypeService;
    private final ExpenseTypeService expenseTypeService;
    public ItemService(
            UnitTypeService unitTypeService,
            ItemCategoryService itemCategoryService,
            UserService userService,
            ExpenseService expenseService,
            ItemRepo itemRepo,
            ExpenseAddressService expenseAddressService,
            ExpenseTypeService expenseTypeService,
            ExpensePaymentTypeService expensePaymentTypeService
    ) {
        this.userService = userService;
        this.itemCategoryService = itemCategoryService;
        this.unitTypeService = unitTypeService;
        this.expenseService = expenseService;
        this.itemRepo = itemRepo;
        this.expenseAddressService = expenseAddressService;
        this.expensePaymentTypeService = expensePaymentTypeService;
        this.expenseTypeService = expenseTypeService;
    }

    /**
     *
     * @param item
     * @return
     */
    public Item save(Item item){
        return this.itemRepo.save(item);
    }

    /**
     *
     * @param item
     * @param unitTypeId
     * @param itemCategoryId
     * @return
     */
    public Optional<Item> createIfNotExists(
            Item item,
            Long unitTypeId,
            Long itemCategoryId,
            Long expenseId
    ){

        if(this.itemRepo.existsByName(item.getName())){
            return Optional.empty();
        }

        Optional<UnitType> unitTypeOpt = this.unitTypeService.findById(unitTypeId);
        Optional<ItemCategory> itemCategoryOpt = this.itemCategoryService.findById(itemCategoryId);
        Optional<Expense> expenseOpt = this.expenseService.findById(expenseId);

        if(unitTypeOpt.isPresent() && itemCategoryOpt.isPresent() && expenseOpt.isPresent()) {

            UnitType unitType = unitTypeOpt.get();
            ItemCategory itemCategory = itemCategoryOpt.get();
            Expense expense = expenseOpt.get();

            unitType.addItem(item);
            item.setUnitType(unitType);

            itemCategory.addItem(item);
            item.setItemCategory(itemCategory);

            expense.addItem(item);
            item.getExpenses().add(expense);

            UserDetailsImpl userDetails = this.userService.getUserDetails();
            item.setCreatedBy(userDetails.getUsername());
            item.setUpdatedBy(userDetails.getUsername());

//            Expense savedExpense = this.expenseService.save(expense);
            Item savedItem = this.save(item);
//            expense.addItem(savedItem);
            //TODO add appropriate return
            return Optional.of(savedItem);

        }
        return Optional.empty();

    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.itemRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Item> findById(Long id){
        return this.itemRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<Item> findAll(){
        return this.itemRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Item> deleteById(Long id){

        Optional<Item> itemOpt = this.itemRepo.findById(id);

        if(itemOpt.isPresent()){
            Item item = itemOpt.get();
            for (Expense expense : item.getExpenses()){
                expense.removeItem(item);
            }
            return Optional.of(this.itemRepo.save(item));
        }
        return Optional.empty();
    }

    /**
     *
     * @param item
     * @param itemCategory
     * @param unitType
     * @return
     */
    public Item update(Item item, ItemCategory itemCategory, UnitType unitType ){

        item.setItemCategory(itemCategory);
        item.setUnitType(unitType);

        return this.save(item);
    }
}
