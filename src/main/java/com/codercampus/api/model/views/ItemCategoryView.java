package com.codercampus.api.model.views;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;


@Entity(name = "itemcatpricesum100")
@Table(name = "itemcatpricesum100")
@Getter
@Setter
// Prevent changes from being applied by Hibernate
@org.hibernate.annotations.Immutable
public class ItemCategoryView {

    @Id
    @Column(name = "categoryid", updatable = false, nullable = false)
    private Long categoryid;

    @Column(name = "category")
    private String category;

    @Column(name = "total")
    private String total;

    @Column(name = "uid")
    private Long uid;
}
