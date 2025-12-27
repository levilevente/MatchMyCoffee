package com.matchmycoffee.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "origins")
public class Origin extends BaseEntity {

    @Column(name = "region", length = 100, nullable = false, unique = true)
    private String region;

    @Column(name = "continent", length = 50)
    private String continent;
}
