package com.codercampus.api.payload.response.responsedto;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.ExpenseItem;
import com.codercampus.api.model.Item;
import com.codercampus.api.model.ItemCategory;
import com.codercampus.api.model.compositeId.ExpenseItemId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ExpenseItemResponseDto {

    private ExpenseItemId id;
    private ItemResponseDto item;

    private ItemCategoryResponseDto itemCategory;
    private UnitTypeResponseDto unitType;
    private BigDecimal amount;
    private BigDecimal unitPrice;
    private BigDecimal price;


    private String createdBy;
    private String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedAt;
}
