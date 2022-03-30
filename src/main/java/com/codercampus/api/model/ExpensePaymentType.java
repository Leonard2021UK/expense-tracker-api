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
import java.util.Objects;
import java.util.Set;

@Entity(name = "expense_payment_type")
@Data
@Table(	name = "expense_payment_type")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ExpensePaymentType {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String name;

    @ManyToOne
    User user;

    @OneToMany(mappedBy = "expensePaymentType",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @JsonIgnore

    Set<Expense> expenses = new HashSet<>();

    public void addExpense(Expense expense) {
        expenses.add( expense );
        expense.setExpensePaymentType( this );
    }

    public void removeExpense(Expense expense) {
        expenses.remove( expense );
        expense.setExpensePaymentType( null );
    }

    private String createdBy;
    private String updatedBy;

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
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExpensePaymentType expensePaymentType = (ExpensePaymentType) o;
        return id != null && Objects.equals(id, expensePaymentType.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
