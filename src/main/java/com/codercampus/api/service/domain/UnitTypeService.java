package com.codercampus.api.service.domain;

import com.codercampus.api.model.Item;
import com.codercampus.api.model.UnitType;
import com.codercampus.api.repository.resource.UnitTypeRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UnitTypeService {

    private final UserService userService;
    private final UnitTypeRepo unitTypeRepo;


    /**
     *
     * @param userService
     * @param unitTypeRepo
     */
    public UnitTypeService(UserService userService, UnitTypeRepo unitTypeRepo) {
        this.userService = userService;
        this.unitTypeRepo = unitTypeRepo;
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

            for(Item item : unitType.getItems()){
                unitType.removeItem(item);
            }
            this.unitTypeRepo.delete(unitType);
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
