package com.codercampus.api.model.views;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "etsumview")
@Table(name = "etsumview")
@Getter
@Setter
// Prevent changes from being applied by Hibernate
@org.hibernate.annotations.Immutable
public class ExpenseTrackerView {

    @Id
    @Column(name = "etid", updatable = false, nullable = false)
    private Long etid;

    @Column(name = "sum")
    private String sum;

    @Column(name = "name")
    private String name;

    @Column(name = "uid")
    private Long uid;

    @Column(name = "created")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created;



}
