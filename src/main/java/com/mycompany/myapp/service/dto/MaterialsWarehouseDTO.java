package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

/**
 * A DTO for the MaterialsWarehouse entity.
 */
public class MaterialsWarehouseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private Long id;

    @Size(min = 1, max = 64)
    private String title;

    @PositiveOrZero
    private Integer left;

    @Positive(message = "Maximum amount that warehouse can hold should be positive")
    private Integer maxAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MaterialsWarehouseDTO materialsWarehouseDTO = (MaterialsWarehouseDTO) o;
        if (materialsWarehouseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), materialsWarehouseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MaterialsWarehouseDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", left=" + getLeft() +
            ", maxAmount=" + getMaxAmount() +
            "}";
    }
}
