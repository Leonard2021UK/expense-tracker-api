package com.codercampus.api.payload.response.responsedto;

import com.codercampus.api.model.Expense;
import com.codercampus.api.model.Item;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class ExpenseResponseDto {

    private Long id;
//    private Long expenseTrackerId;
    private ExpenseTypeResponseDto expenseType;
    private ExpenseAddressResponseDto expenseAddress;
    private ExpensePaymentTypeResponseDto expensePaymentType;
    private Set<ExpenseItemResponseDto> expenseItems;

    private String expenseName;
    private String expenseEmail;
    private String expenseComment;
    private String expenseMobile;
    private String expensePhone;


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
