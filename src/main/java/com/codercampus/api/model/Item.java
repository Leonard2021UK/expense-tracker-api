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
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity(name = "item")
@Table(	name = "item")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String createdBy;
    private String updatedBy;

    @Size(min = 3, max = 50)
    private String name;
//    private BigDecimal amount;
//    private BigDecimal unitPrice;
//
    private boolean isArchived = false;

    @ManyToOne
    private User user;

//    @ManyToOne
//    private UnitType unitType;
//
//    @ManyToOne
//    private ItemCategory itemCategory;

    @OneToMany(
            mappedBy = "item",
            orphanRemoval = true
    )
    @ToString.Exclude
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<ExpenseItem> expenseItems = new HashSet<>();

    public void addExpense(ExpenseItem expenseItem) {

        this.expenseItems.add(expenseItem);
        expenseItem.setItem(this);
    }

    public void removeExpense(Expense expense) {
        for (Iterator<ExpenseItem> iterator = expenseItems.iterator();
             iterator.hasNext(); ) {

            ExpenseItem expenseItem = iterator.next();


            if (expenseItem.getItem().equals(this) && expenseItem.getExpense().equals(expense)) {
                iterator.remove();
                expenseItem.getExpense().getExpenseItems().remove(expenseItem);
                expenseItem.setItem(null);
                expenseItem.setExpense(null);
            }else{
                System.out.println("dddd");
            }
        }
    }
//    @ManyToMany(mappedBy = "items")
//    @ToString.Exclude
//    @JsonIgnore
//    private Set<Expense> expenses = new HashSet<>();

//    @PreRemove
//    private void dismantleItem(){
//
//        this.user.removeItem( this );
////        this.itemCategory.removeItem( this );
////        this.unitType.removeItem( this );
//
//        for (Expense expense : this.expenses){
//            expense.removeItem(this);
//        }
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
        if (!(o instanceof Item)) return false;
//        if (o == null || getClass() != o.getClass()) return false;

        Item that = (Item) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
