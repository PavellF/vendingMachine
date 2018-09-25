package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Size;

/**
 * A DTO for the Coffee entity.
 */
public class CoffeeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

	private Long id;

    @Size(min = 1, max = 64)
    private String title;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CoffeeDTO coffeeDTO = (CoffeeDTO) o;
        if (coffeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), coffeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CoffeeDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            "}";
    }
}
