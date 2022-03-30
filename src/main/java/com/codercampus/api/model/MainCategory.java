package com.codercampus.api.model;

import com.codercampus.api.security.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

@Entity(name = "main_category")
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

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "mainCategory",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    @JsonIgnore
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
        if (!(o instanceof MainCategory)) return false;
//        if (o == null || getClass() != o.getClass()) return false;

        MainCategory that = (MainCategory) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
