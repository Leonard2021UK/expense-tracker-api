package com.codercampus.api.service.domain;

import com.codercampus.api.exception.ResourceHasReferenceException;
import com.codercampus.api.exception.ResourceNotFoundException;
import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseAddress;
import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.repository.resource.ExpenseAddressRepo;
import com.codercampus.api.repository.resource.ExpenseRepo;
import com.codercampus.api.security.UserDetailsImpl;
import com.codercampus.api.service.UserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseAddressService {

    private final UserService userService;
    private final ExpenseTrackerService expenseTrackerService;
    private final ExpenseAddressRepo expenseAddressRepo;

    public ExpenseAddressService(
            UserService userService,
            ExpenseTrackerService expenseTrackerService,
            ExpenseAddressRepo expenseAddressRepo
    ) {
        this.userService = userService;
        this.expenseTrackerService = expenseTrackerService;
        this.expenseAddressRepo = expenseAddressRepo;
    }

    /**
     *
     * @param expenseAddress
     * @return
     */
    public ExpenseAddress save(ExpenseAddress expenseAddress){
        return this.expenseAddressRepo.save(expenseAddress);
    }

    /**
     *
     * @param expenseAddress
     * @return
     */
    public Optional<ExpenseAddress> createIfNotExists(ExpenseAddress expenseAddress){

        if(this.expenseAddressRepo.existsByName(expenseAddress.getName())){
            return Optional.empty();
        }

        UserDetailsImpl userDetails = this.userService.getUserDetails();
        expenseAddress.setUser(userDetails.getUser());
        expenseAddress.setCreatedBy(userDetails.getUsername());
        expenseAddress.setUpdatedBy(userDetails.getUsername());
//        expenseAddress.addExpense(expense);
        return Optional.of(this.save(expenseAddress));
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean isExists(String name){
        return this.expenseAddressRepo.existsByName(name);
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<ExpenseAddress> findById(Long id){
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.expenseAddressRepo.findByIdAndUserId(id,userDetails.getUser().getId());
    }

    /**
     *
     * @return
     */
    public List<ExpenseAddress> findAll(){
        UserDetailsImpl userDetails = this.userService.getUserDetails();
        return this.expenseAddressRepo.findAllByUserId(userDetails.getUser().getId());
    }

    /**
     *
     * @param id
     * @return
     */
    @Transactional
    public ExpenseAddress deleteById(Long id) throws ResourceHasReferenceException, ResourceNotFoundException {

        Long currentUserId = this.userService.getUserDetails().getUser().getId();
        Optional<ExpenseAddress> expenseAddressOpt = this.expenseAddressRepo.findByIdAndUserId(id,currentUserId);

        if(expenseAddressOpt.isPresent()){

            ExpenseAddress expenseAddress = expenseAddressOpt.get();

            if(expenseAddress.getExpenses().isEmpty()){
//                for(Expense expense : expenseAddress.getExpenses()){
//                    expenseAddress.removeExpense(expense);
//                }
                this.expenseAddressRepo.deleteById(id,currentUserId);

                return expenseAddress;
            }else{
                throw ResourceHasReferenceException.createWith("Expense address");

            }

        }
        throw ResourceNotFoundException.createWith("Expense address could not be found!");
    }

    /**
     *
     * @param expenseAddress
     * @return
     */
    public ExpenseAddress update(ExpenseAddress expenseAddress ){


        return this.save(expenseAddress);
    }
}
