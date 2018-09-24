package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A MaterialsWarehouse.
 */
@Entity
@Table(name = "materials_warehouse")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MaterialsWarehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @Size(min = 1, max = 64)
    @NotNull
    private String title;

    @Column(name = "materials_left")
    @NotNull
    @PositiveOrZero
    private Integer left;

    @Column(name = "max_amount")
    @Positive
    @NotNull
    private Integer maxAmount;

    @OneToMany(mappedBy = "materialsWarehouse")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CofeeMaterial> materials = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public MaterialsWarehouse title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLeft() {
        return left;
    }

    public MaterialsWarehouse left(Integer left) {
        this.left = left;
        return this;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public MaterialsWarehouse maxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
        return this;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Set<CofeeMaterial> getMaterials() {
        return materials;
    }

    public MaterialsWarehouse materials(Set<CofeeMaterial> cofeeMaterials) {
        this.materials = cofeeMaterials;
        return this;
    }

    public MaterialsWarehouse addMaterials(CofeeMaterial cofeeMaterial) {
        this.materials.add(cofeeMaterial);
        cofeeMaterial.setMaterialsWarehouse(this);
        return this;
    }

    public MaterialsWarehouse removeMaterials(CofeeMaterial cofeeMaterial) {
        this.materials.remove(cofeeMaterial);
        cofeeMaterial.setMaterialsWarehouse(null);
        return this;
    }

    public void setMaterials(Set<CofeeMaterial> cofeeMaterials) {
        this.materials = cofeeMaterials;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MaterialsWarehouse materialsWarehouse = (MaterialsWarehouse) o;
        if (materialsWarehouse.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialsWarehouse.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialsWarehouse{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", left=" + getLeft() +
            ", maxAmount=" + getMaxAmount() +
            "}";
    }
}
