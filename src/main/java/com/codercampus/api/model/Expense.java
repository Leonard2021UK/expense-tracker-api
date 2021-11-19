package com.codercampus.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(	name = "expense")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Expense {

    @Id
    private Long id;

    private String name;

    private String extraInfo;

    private String phoneNumber;

    private String email;

    private String mobileNumber;

    private String createdBy;

    private String updatedBy;

    @ManyToOne
    private ExpenseTracker expenseTracker;

//    @ManyToOne
//    private ExpensePaymentType expensePaymentType;
//
//    @ManyToOne
//    private ExpenseAddress expenseAddress;
//
//    @ManyToOne
//    private ExpenseType expenseType;

//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @ToString.Exclude
//    private Set<Item> items = new HashSet<>();
//
//    public void addItem(Item item){
//        items.add(item);
//        item.getExpense().add(this);
//    }
//
//    public void removeItem(Item item){
//        items.remove(item);
//        item.getExpense().remove(this);
//    }


    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        return name.equals(expense.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
