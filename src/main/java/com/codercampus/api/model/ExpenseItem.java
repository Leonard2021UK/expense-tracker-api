package com.codercampus.api.model;

import com.codercampus.api.model.compositeId.ExpenseItemId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity(name = "expenseItem")
@NoArgsConstructor(access= AccessLevel.PUBLIC)
public class ExpenseItem {

    @EmbeddedId
    private ExpenseItemId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("expenseId")
//    @JsonIgnore
    @ToString.Exclude
    private Expense expense;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
//    @JsonIgnore
    @ToString.Exclude
    private Item item;

    public ExpenseItem(Expense expense, Item item, Long rowId){
        this.expense = expense;
        this.item = item;
        this.id = new ExpenseItemId(expense.getId(),item.getId(), rowId);
    }
    @ManyToOne
    private UnitType unitType;

    @ManyToOne
    private ItemCategory itemCategory;

    private BigDecimal amount;
    private BigDecimal unitPrice;
    private BigDecimal price;

    private boolean isArchived = false;


    private String createdBy;
    private String updatedBy;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;


}
