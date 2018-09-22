package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the MaterialsWarehouse entity.
 */
public class MaterialsWarehouseDTO implements Serializable {

    private Long id;

    private String title;

    private Integer left;

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
