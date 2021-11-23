package com.codercampus.api.payload.request.requestdto;

import com.codercampus.api.model.ExpenseTracker;
import com.codercampus.api.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class MainCategoryRequestDto {

    Long id;

    String name;

    Long userId;

//    @OneToMany(mappedBy = "mainCategory",cascade = CascadeType.ALL, orphanRemoval = true)
//    @ToString.Exclude
//    Set<ExpenseTracker> expenseTrackers = new HashSet<>();

    private String createdBy;
    private String updatedBy;

//    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using =LocalDateTimeSerializer.class)
    @Column(updatable = false)
    private LocalDateTime createdAt;

//    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using =LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;
}
