package com.codercampus.api.model;

import com.codercampus.api.security.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(	name = "main_category")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@DynamicUpdate
public class MainCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Size(min = 3,max = 100)
    @NotNull
    String name;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "mainCategory",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    Set<ExpenseTracker> expenseTrackers = new HashSet<>();

    public void addExpenseTracker(ExpenseTracker expenseTracker) {
        expenseTrackers.add( expenseTracker );
        expenseTracker.setMainCategory( this );
    }

    public void removeExpenseTracker(ExpenseTracker expenseTracker) {
        expenseTrackers.remove( expenseTracker );
        expenseTracker.setMainCategory( null );
    }

    private String createdBy;
    private String updatedBy;


    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Item item = (Item) o;
        return id != null && Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
