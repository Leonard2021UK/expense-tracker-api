package com.codercampus.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    @JsonIgnore
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    private Set<ExpenseTracker> expenseTrackers = new HashSet<>();

    public void addExpenseTracker(ExpenseTracker expenseTracker){
        expenseTrackers.add(expenseTracker);
        expenseTracker.setUser(this);
    }

    public void removeExpenseTracker(ExpenseTracker expenseTracker){
        expenseTrackers.remove(expenseTracker);
        expenseTracker.setUser(null);
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    Set<MainCategory> mainCategories = new HashSet<>();

    public void addMainCategory(MainCategory mainCategory) {
        mainCategories.add( mainCategory );
        mainCategory.setUser( this );
    }

    public void removeMainCategory(MainCategory mainCategory) {
        mainCategories.remove( mainCategory );
        mainCategory.setUser( null );
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    Set<ExpenseType> expenseTypes = new HashSet<>();

    public void addExpenseType(ExpenseType expenseType) {
        expenseTypes.add( expenseType );
        expenseType.setUser( this );
    }

    public void removeExpenseType(ExpenseType expenseType) {
        expenseTypes.remove( expenseType );
        expenseType.setUser( null );
    }


    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    Set<ExpensePaymentType> expensePaymentTypes = new HashSet<>();

    public void addExpensePaymentType(ExpensePaymentType expensePaymentType) {
        expensePaymentTypes.add( expensePaymentType );
        expensePaymentType.setUser( this );
    }

    public void removeExpensePaymentType(ExpensePaymentType expensePaymentType) {
        expensePaymentTypes.remove( expensePaymentType );
        expensePaymentType.setUser( null );
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    Set<ExpenseAddress> expenseAddresses = new HashSet<>();

    public void addExpenseAddress(ExpenseAddress expenseAddress) {
        expenseAddresses.add( expenseAddress );
        expenseAddress.setUser( this );
    }

    public void removeExpenseAddress(ExpenseAddress expenseAddress) {
        expenseAddresses.remove( expenseAddress );
        expenseAddress.setUser( null );
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    Set<Item> items = new HashSet<>();

    public void addItem(Item item) {
        items.add( item );
        item.setUser( this );
    }

    public void removeItem(Item item) {
        items.remove( item );
        item.setUser( null );
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    Set<UnitType> unitTypes = new HashSet<>();

    public void addUnitType(UnitType unitType) {
        unitTypes.add( unitType );
        unitType.setUser( this );
    }

    public void removeUnitType(UnitType unitType) {
        unitTypes.remove( unitType );
        unitType.setUser( null );
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "user",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore
    Set<ItemCategory> itemCategories = new HashSet<>();

    public void addItemCategory(ItemCategory itemCategory) {
        itemCategories.add( itemCategory );
        itemCategory.setUser( this );
    }

    public void removeItemCategory(ItemCategory itemCategory) {
        itemCategories.remove( itemCategory );
        itemCategory.setUser( null );
    }
}