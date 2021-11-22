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

    public void removeExpenseTracker(MainCategory mainCategory) {
        mainCategories.remove( mainCategory );
        mainCategory.setUser( null );
    }


}