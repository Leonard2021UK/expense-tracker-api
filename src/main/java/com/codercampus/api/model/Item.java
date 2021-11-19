package com.codercampus.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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

@Entity
@Table(	name = "item")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Item {

    @Id
    Long id;

    private String createdBy;
    private String updatedBy;

    String name;

    @ManyToOne
    UnitType unitType;

    @ManyToOne
    ItemCategory itemCategory;

//    @ManyToMany(mappedBy = "items")
//    @ToString.Exclude
//    private Set<Expense> expense = new HashSet<>();

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
        Item item = (Item) o;
        return  Objects.equals(name, item.name) &&
                Objects.equals(itemCategory.name, item.itemCategory.name);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
