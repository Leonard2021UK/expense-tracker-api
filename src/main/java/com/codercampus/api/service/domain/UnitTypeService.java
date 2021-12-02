package com.codercampus.api.service.domain;

import com.codercampus.api.model.Item;
import com.codercampus.api.model.UnitType;
import com.codercampus.api.model.User;
import com.codercampus.api.repository.resource.ItemRepo;
import com.codercampus.api.repository.resource.UnitTypeRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UnitTypeService {

    private final UserService userService;
    private final UnitTypeRepo unitTypeRepo;
    private final ItemRepo itemRepo;


    /**
     *
     * @param userService
     * @param unitTypeRepo
     */
    public UnitTypeService(UserService userService, UnitTypeRepo unitTypeRepo, ItemRepo itemRepo) {
        this.userService = userService;
        this.unitTypeRepo = unitTypeRepo;
        this.itemRepo = itemRepo;
    }

    /**
     *
     * @param unitType
     * @return
     */
    public UnitType save(UnitType unitType){
        User currentUser = this.userService.getUserDetails().getUser();
        unitType.setUser(currentUser);
        return this.unitTypeRepo.save(unitType);
    }

    /**
     *
     * @param unitType
     * @return
     */
    public Optional<UnitType> createIfNotExists(UnitType unitType){
        if(this.unitTypeRepo.existsByName(unitType.getName())){
            return Optional.empty();
        }else{

            UserDetailsImpl userDetails = this.userService.getUserDetails();

            unitType.setCreatedBy(userDetails.getUsername());
            unitType.setUpdatedBy(userDetails.getUsername());
            unitType.setUser(userDetails.getUser());

            userDetails.getUser().addUnitType(unitType);
            return Optional.of(this.unitTypeRepo.save(unitType));
        }

    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.unitTypeRepo.existsByName(name);
    }

    /**
     *
     * @param unitTypeId
     * @return
     */
    public Optional<UnitType> findById(Long unitTypeId){
        Long currentUserId = this.userService.getUserDetails().getUser().getId();

        return this.unitTypeRepo.findById(unitTypeId,currentUserId);
    }

    /**
     *
     * @return
     */
    public List<UnitType> findAllNoneArchived(){
        Long currentUserId = this.userService.getUserDetails().getUser().getId();
        return this.unitTypeRepo.findAllNoneArchived(currentUserId);
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public Optional<UnitType> archiveOrDeleteById(Long id){

        Optional<UnitType> unitTypeOpt = this.unitTypeRepo.findById(id);

        if(unitTypeOpt.isPresent()){

            Long currentUserId = this.userService.getUserDetails().getUser().getId();
            UnitType unitType = unitTypeOpt.get();

            // if there are no references (not used by any item) remove from the database
            // otherwise only marked as archived
            if(unitType.getItems().isEmpty()){
                this.unitTypeRepo.deleteById(unitType.getId(),currentUserId);
                return Optional.of(unitType);
            }

            unitType.setArchived(true);

            return Optional.of(unitType);
            //TODO appropriate Error message
//            return Optional.empty();
        }
        return unitTypeOpt;
    }

    /**
     *
     * @param unitType
     * @return
     */
    public UnitType update(UnitType unitType){

        UserDetailsImpl userDetails = this.userService.getUserDetails();
        unitType.setUser(userDetails.getUser());
        unitType.setUpdatedBy(userDetails.getUsername());

        return this.save(unitType);
    }
}
