package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Positive;

/**
 * A DTO for the CofeeMaterial entity.
 */
@SuppressWarnings("serial")
public class CofeeMaterialDTO implements Serializable {

    private Long id;

    @Positive
    private Integer amount;

    private Long coffeeId;

    private Long materialsWarehouseId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getCoffeeId() {
        return coffeeId;
    }

    public void setCoffeeId(Long coffeeId) {
        this.coffeeId = coffeeId;
    }

    public Long getMaterialsWarehouseId() {
        return materialsWarehouseId;
    }

    public void setMaterialsWarehouseId(Long materialsWarehouseId) {
        this.materialsWarehouseId = materialsWarehouseId;
    }

    /**
     * Sets values from another object on null fields.
     * @param with source or values (they can be null as well)
     * */
    public void mergeNull(CofeeMaterialDTO with) {
        if (id == null) {
            setId(with.getId());
        }
        if (amount == null) {
            setAmount(with.getAmount());
        }
        if (coffeeId == null) {
            setCoffeeId(with.getCoffeeId());
        }
        if (materialsWarehouseId == null) {
            setMaterialsWarehouseId(with.getMaterialsWarehouseId());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CofeeMaterialDTO cofeeMaterialDTO = (CofeeMaterialDTO) o;
        if (cofeeMaterialDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cofeeMaterialDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CofeeMaterialDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", coffee=" + getCoffeeId() +
            ", materialsWarehouse=" + getMaterialsWarehouseId() +
            "}";
    }
}
