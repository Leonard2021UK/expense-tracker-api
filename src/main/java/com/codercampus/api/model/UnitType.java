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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    private User user;

    private boolean isArchived = false;

    @OneToMany(
            mappedBy = "unitType",
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

    public void addItem(ExpenseItem item) {
        items.add( item );
        item.setUnitType( this );
    }

    public void removeItem(ExpenseItem item) {
        items.remove( item );
        item.setUnitType( null );
    }

    @PreRemove
    private void removeUnitTypeFromItem(){
        this.user.removeUnitType( this );
//        this.user = null;
        for (ExpenseItem item :items){
            this.removeItem( item );
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
        UnitType unitType = (UnitType) o;
        return id != null && Objects.equals(id, unitType.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
