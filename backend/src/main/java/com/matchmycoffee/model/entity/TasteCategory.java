package com.matchmycoffee.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@ToString(callSuper = true)
@Table(name = "taste_categories")
public class TasteCategory extends BaseEntity {

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "color_code", length = 7, nullable = false)
    private String colorCode;

    @OneToMany(mappedBy = "category")
    private List<Taste> tastes;
}
