package com.codercampus.api.service.domain;

import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.ItemCategory;
import com.codercampus.api.model.User;
import com.codercampus.api.repository.resource.ItemCategoryRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        User currentUser = this.userService.getUserDetails().getUser();
        itemCategory.setUser(currentUser);
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
        }

        UserDetailsImpl userDetails = this.userService.getUserDetails();

        itemCategory.setCreatedBy(userDetails.getUsername());
        itemCategory.setUpdatedBy(userDetails.getUsername());
        itemCategory.setUser(userDetails.getUser());

        userDetails.getUser().addItemCategory(itemCategory);
        return Optional.of(this.itemCategoryRepo.save(itemCategory));

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
     * @param itemCategoryId
     * @return
     */
    public Optional<ItemCategory> findById(Long itemCategoryId){
        Long currentUserId = this.userService.getUserDetails().getUser().getId();
        return this.itemCategoryRepo.findByIdAndUserId(itemCategoryId,currentUserId);
    }

    /**
     *
     *
     * @return
     */
    public List<ItemCategory> findAll(){
        Long currentUserId = this.userService.getUserDetails().getUser().getId();

        return this.itemCategoryRepo.findAllByUserId(currentUserId);
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public ItemCategory deleteById(Long id) throws ResourceNotFoundException, ResourceHasReferenceException {

        Long currentUserId = this.userService.getUserDetails().getUser().getId();
        Optional<ItemCategory> itemCategoryOpt = this.itemCategoryRepo.findByIdAndUserId(id,currentUserId);

        if(itemCategoryOpt.isPresent()){

            ItemCategory itemCategory = itemCategoryOpt.get();

            if(itemCategory.getItems().isEmpty()){

                this.itemCategoryRepo.deleteById(itemCategory.getId(),currentUserId);

                return itemCategory;

            }else{

                throw ResourceHasReferenceException.createWith("Item category");

            }
            //TODO appropriate Error message
//            return Optional.empty();
        }
        throw ResourceNotFoundException.createWith("Item category could not be found!");
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
