package com.codercampus.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.bytebuddy.build.ToStringPlugin;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "expense_tracker")
@Table(	name = "expense_tracker")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ExpenseTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @NotNull
    private MainCategory mainCategory;

    @ManyToOne
    private User user;
//
//    public void setUser(User user) {
//        this.user = user;
//    }

    @OneToMany(mappedBy = "expenseTracker", orphanRemoval = true)
    private Set<Expense> expenses = new HashSet<>();

    public void addExpense(Expense expense){
        expenses.add(expense);
        expense.setExpenseTracker( this );
    }

    public void removeExpense(Expense expense){
        expenses.remove(expense);
        expense.setExpenseTracker( null );
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
        if (!(o instanceof ExpenseTracker)) return false;
//        if (o == null || getClass() != o.getClass()) return false;

        ExpenseTracker that = (ExpenseTracker) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }


}
