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
@Table(name = "brewing_methods")
public class BrewingMethod extends BaseEntity {

    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url", length = 255)
    private String iconUrl;
}
