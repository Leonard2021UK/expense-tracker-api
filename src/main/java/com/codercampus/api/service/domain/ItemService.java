package com.codercampus.api.service.domain;

import com.codercampus.api.model.*;
import com.codercampus.api.repository.resource.ExpenseRepo;
import com.codercampus.api.repository.resource.ItemRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final UserService userService;
    private final ExpenseService expenseService;
    private final ItemRepo itemRepo;
    private final UnitTypeService unitTypeService;
    private final ItemCategoryService itemCategoryService;

    public ItemService(
            UnitTypeService unitTypeService,
            ItemCategoryService itemCategoryService,
            UserService userService,
            ExpenseService expenseService,
            ItemRepo itemRepo
    ) {
        this.userService = userService;
        this.itemCategoryService = itemCategoryService;
        this.unitTypeService = unitTypeService;
        this.expenseService = expenseService;
        this.itemRepo = itemRepo;
    }

    /**
     *
     * @param item
     * @return
     */
    public Item save(Item item){
        User currentUser = this.userService.getUserDetails().getUser();
        item.setUser(currentUser);
        return this.itemRepo.save(item);
    }

    /**
     *
//     * @param item
//     * @param unitTypeId
//     * @param itemCategoryId
     * @return
     */
    public Optional<Item> createIfNotExists(Item item){
//        public Optional<Item> createIfNotExists(
//                Item item,
//                Long unitTypeId,
//                Long itemCategoryId,
//                Long expenseId
//    ){
        if(this.itemRepo.existsByName(item.getName())){
            return Optional.empty();
        }

//        Optional<UnitType> unitTypeOpt = this.unitTypeService.findById(unitTypeId);
//        Optional<ItemCategory> itemCategoryOpt = this.itemCategoryService.findById(itemCategoryId);
//        Optional<Expense> expenseOpt = this.expenseService.findById(expenseId);

//        if(unitTypeOpt.isPresent() && itemCategoryOpt.isPresent() && expenseOpt.isPresent()) {

//            UnitType unitType = unitTypeOpt.get();
//            ItemCategory itemCategory = itemCategoryOpt.get();
//            Expense expense = expenseOpt.get();
            User currentUser = this.userService.getUserDetails().getUser();

//            unitType.addItem(item);
//            item.setUnitType(unitType);

//            itemCategory.addItem(item);
//            item.setItemCategory(itemCategory);

//            expense.addItem(item);
//            item.getExpenses().add(expense);

            item.setUser(currentUser);
            item.setCreatedBy(currentUser.getUsername());
            item.setUpdatedBy(currentUser.getUsername());

//            Expense savedExpense = this.expenseService.save(expense);
            Item savedItem = this.save(item);
//            expense.addItem(savedItem);
            //TODO add appropriate return
            return Optional.of(savedItem);

//        }
//        return Optional.empty();

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
    public List<Item> findAllNoneArchived(){
        Long currentUserId = this.userService.getUserDetails().getUser().getId();

        return this.itemRepo.findAllNoneArchived(currentUserId);
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public Optional<Item> archiveOrDeleteById(Long id){

        Optional<Item> itemOpt = this.itemRepo.findById(id);

        if(itemOpt.isPresent()){
            Long currentUserId = this.userService.getUserDetails().getUser().getId();
            Item item = itemOpt.get();

            this.itemRepo.deleteById(item.getId(),currentUserId);
            return Optional.of(item);
        }
        return Optional.empty();
    }

    /**
     *
     * @param item
     * @return
     */
    public Item update(Item item){
        return this.save(item);
    }
}
