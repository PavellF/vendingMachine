package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity Coffee and its DTO CoffeeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CoffeeMapper extends EntityMapper<CoffeeDTO, Coffee> {

	 @Mapping(target = "cofees", ignore = true)
	 Coffee toEntity(CoffeeDTO coffeeDTO);
	
	default Coffee fromId(Long id) {
        if (id == null) {
            return null;
        }
        Coffee coffee = new Coffee();
        coffee.setId(id);
        return coffee;
    }
}
