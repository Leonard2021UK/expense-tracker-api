package com.codercampus.api.service.domain;

import com.codercampus.api.model.Item;
import com.codercampus.api.model.UnitType;
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
     * @param id
     * @return
     */
    public Optional<UnitType> findById(Long id){
        return this.unitTypeRepo.findById(id);
    }

    /**
     *
     * @return
     */
    public List<UnitType> findAll(){
        return this.unitTypeRepo.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<UnitType> deleteById(Long id){

        Optional<UnitType> unitTypeOpt = this.unitTypeRepo.findById(id);

        if(unitTypeOpt.isPresent()){

            UnitType unitType = unitTypeOpt.get();
//            unitType.setUser(null);
//            this.unitTypeRepo.save()
            if(unitType.getItems().isEmpty()){
            }


            this.unitTypeRepo.deleteById(unitType.getId());
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
