package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import java.io.Serializable;
import java.util.Objects;

/**
 * A CofeeMaterial.
 */
@Entity
@Table(name = "cofee_material")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CofeeMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount_needed")
    @PositiveOrZero
    @NotNull
    private Integer amount;

    @ManyToOne
    @JsonIgnoreProperties("cofees")
    private Coffee coffee;

    @ManyToOne
    @JsonIgnoreProperties("materials")
    private MaterialsWarehouse materialsWarehouse;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public CofeeMaterial amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Coffee getCoffee() {
        return coffee;
    }

    public CofeeMaterial coffee(Coffee coffee) {
        this.coffee = coffee;
        return this;
    }

    public void setCoffee(Coffee coffee) {
        this.coffee = coffee;
    }

    public MaterialsWarehouse getMaterialsWarehouse() {
        return materialsWarehouse;
    }

    public CofeeMaterial materialsWarehouse(MaterialsWarehouse materialsWarehouse) {
        this.materialsWarehouse = materialsWarehouse;
        return this;
    }

    public void setMaterialsWarehouse(MaterialsWarehouse materialsWarehouse) {
        this.materialsWarehouse = materialsWarehouse;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CofeeMaterial cofeeMaterial = (CofeeMaterial) o;
        if (cofeeMaterial.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cofeeMaterial.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CofeeMaterial{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
