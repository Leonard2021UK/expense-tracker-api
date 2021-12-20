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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
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

    private String name;
//    private BigDecimal amount;
//    private BigDecimal unitPrice;
//
//    private boolean isArchived = false;

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
    private Set<ExpenseItem> expenses = new HashSet<>();

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
