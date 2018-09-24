package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CoffeeDTO;

/**
 * Mapper for the entity Coffee and its DTO CoffeeDTO.
 */
public interface CoffeeMapper extends EntityMapper<CoffeeDTO, Coffee> {

	default Coffee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coffee coffee = new Coffee();
        coffee.setId(id);
        return coffee;
    }
}
