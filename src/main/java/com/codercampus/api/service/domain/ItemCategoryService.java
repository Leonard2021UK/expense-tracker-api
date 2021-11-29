package com.codercampus.api.service.domain;

import com.codercampus.api.model.ItemCategory;
import com.codercampus.api.repository.resource.ItemCategoryRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemCategoryService {

    private final UserService userService;
    private final ItemCategoryRepo itemCategoryRepo;


    /**
     *
     * @param userService
     * @param itemCategoryRepo
     */
    public ItemCategoryService(UserService userService, ItemCategoryRepo itemCategoryRepo) {
        this.userService = userService;
        this.itemCategoryRepo = itemCategoryRepo;
    }

    /**
     *
     * @param itemCategory
     * @return
     */
    public ItemCategory save(ItemCategory itemCategory){
        return this.itemCategoryRepo.save(itemCategory);
    }

    /**
     *
     * @param itemCategory
     * @return
     */
    public Optional<ItemCategory> createIfNotExists(ItemCategory itemCategory){
        if(this.itemCategoryRepo.existsByName(itemCategory.getName())){
            return Optional.empty();
        }else{

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            itemCategory.setCreatedBy(userDetails.getUsername());
            itemCategory.setUpdatedBy(userDetails.getUsername());

            userDetails.getUser().addItemCategory(itemCategory);
            return Optional.of(this.itemCategoryRepo.save(itemCategory));
        }

    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.itemCategoryRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ItemCategory> findById(Long id){
        return this.itemCategoryRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<ItemCategory> findAll(){
        return this.itemCategoryRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ItemCategory> deleteById(Long id){
        Optional<ItemCategory> itemCategoryOpt = this.itemCategoryRepo.findById(id);

        if(itemCategoryOpt.isPresent()){
            ItemCategory itemCategory = itemCategoryOpt.get();

            itemCategory.getUser().getItemCategories().remove(itemCategory);

            return Optional.of(this.save(itemCategory));
        }
        return Optional.empty();
    }

    /**
     *
     * @param itemCategory
     * @return
     */
    public ItemCategory update(ItemCategory itemCategory){

        UserDetailsImpl userDetails = this.userService.getUserDetails();
        itemCategory.setUser(userDetails.getUser());
        itemCategory.setUpdatedBy(userDetails.getUsername());

        return this.save(itemCategory);
    }
}
