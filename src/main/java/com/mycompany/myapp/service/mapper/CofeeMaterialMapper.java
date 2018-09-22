package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CofeeMaterialDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CofeeMaterial and its DTO CofeeMaterialDTO.
 */
@Mapper(componentModel = "spring", uses = {CoffeeMapper.class, MaterialsWarehouseMapper.class})
public interface CofeeMaterialMapper extends EntityMapper<CofeeMaterialDTO, CofeeMaterial> {

    @Mapping(source = "coffee.id", target = "coffeeId")
    @Mapping(source = "materialsWarehouse.id", target = "materialsWarehouseId")
    CofeeMaterialDTO toDto(CofeeMaterial cofeeMaterial);

    @Mapping(source = "coffeeId", target = "coffee")
    @Mapping(source = "materialsWarehouseId", target = "materialsWarehouse")
    CofeeMaterial toEntity(CofeeMaterialDTO cofeeMaterialDTO);

    default CofeeMaterial fromId(Long id) {
        if (id == null) {
            return null;
        }
        CofeeMaterial cofeeMaterial = new CofeeMaterial();
        cofeeMaterial.setId(id);
        return cofeeMaterial;
    }
}
