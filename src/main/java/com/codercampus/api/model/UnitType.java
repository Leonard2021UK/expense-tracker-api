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
@Table(	name = "unit_type")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UnitType {

    @Id
    Long id;

    String name;

    @OneToMany(mappedBy = "unitType",cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    Set<Item> items = new HashSet<>();

    public void addItem(Item item) {
        items.add( item );
        item.setUnitType( this );
    }

    public void removeExpenseTracker(Item item) {
        items.remove( item );
        item.setUnitType( null );
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
        if (o == null || getClass() != o.getClass()) return false;

        UnitType unitType = (UnitType) o;

        return name.equals(unitType.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
