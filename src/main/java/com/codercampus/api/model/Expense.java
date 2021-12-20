package com.codercampus.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String extraInfo;

    private String phoneNumber;

    private String email;

    private String mobileNumber;

    private String createdBy;

    private String updatedBy;

    @ManyToOne
    @ToString.Exclude
    @JsonIgnore
    private ExpenseTracker expenseTracker;

    @ManyToOne
    private ExpensePaymentType expensePaymentType;

    @ManyToOne
    private ExpenseAddress expenseAddress;

    @ManyToOne
    private ExpenseType expenseType;

    @EqualsAndHashCode.Exclude
    @OneToMany(
            mappedBy = "expense",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ExpenseItem> items = new HashSet<>();

    public void addItem(Item item, ExpenseItem expenseItem) {

        // TODO this clear is necessary?
        this.items.clear();
        this.items.add(expenseItem);

        item.getExpenses().clear();
        item.getExpenses().add(expenseItem);
    }

    public void removeItem(Item item) {
        for (Iterator<ExpenseItem> iterator = items.iterator();
             iterator.hasNext(); ) {

            ExpenseItem expenseItem = iterator.next();


            if (expenseItem.getExpense().equals(this) && expenseItem.getItem().equals(item)) {
                iterator.remove();
                expenseItem.getItem().getExpenses().remove(expenseItem);
                expenseItem.setItem(null);
                expenseItem.setExpense(null);
            }else{
                System.out.println("dddd");
            }
        }
    }
//    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JoinTable(
//            name = "expense_item",
//            joinColumns = { @JoinColumn(name = "expense_id") },
//            inverseJoinColumns = { @JoinColumn(name = "item_id") }
//    )
//    @ToString.Exclude
//    private Set<Item> items = new HashSet<>();

//    public void addItem(Item item){
//        items.add(item);
//        item.getExpenses().add(this);
//    }
//
//    public void removeItem(Item item){
//        items.remove(item);
//        item.getExpenses().remove(this);
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
        if (!(o instanceof Expense)) return false;
//        if (o == null || getClass() != o.getClass()) return false;

        Expense that = (Expense) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
