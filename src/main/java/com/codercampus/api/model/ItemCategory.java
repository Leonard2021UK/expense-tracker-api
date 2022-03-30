package com.codercampus.api.model;

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
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity(name = "item_category")
@Table(	name = "item_category")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ItemCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, max = 50)
    private String name;

    @ManyToOne
    private User user;

//    private boolean isArchived = false;

    @OneToMany(
            mappedBy = "itemCategory",
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.DETACH
            },
            fetch = FetchType.LAZY)
    @ToString.Exclude
    @JsonIgnore
    Set<ExpenseItem> items = new HashSet<>();

    public void addExpenseItem(ExpenseItem expenseItem) {
        items.add( expenseItem );
        expenseItem.setItemCategory( this );
    }

    public void removeExpenseItem(ExpenseItem expenseItem) {
        items.remove( expenseItem );
        expenseItem.setItemCategory( null );
    }

    @PreRemove
    private void removeItemCategoryFromItem(){
        this.user.removeItemCategory( this );
        this.user = null;
        for (ExpenseItem expenseItem :items){
            this.removeExpenseItem( expenseItem );
        }
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
        ItemCategory itemCategory = (ItemCategory) o;
        return id != null && Objects.equals(id, itemCategory.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
