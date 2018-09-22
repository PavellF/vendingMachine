package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity MaterialsWarehouse and its DTO MaterialsWarehouseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MaterialsWarehouseMapper extends EntityMapper<MaterialsWarehouseDTO, MaterialsWarehouse> {


    @Mapping(target = "materials", ignore = true)
    MaterialsWarehouse toEntity(MaterialsWarehouseDTO materialsWarehouseDTO);

    default MaterialsWarehouse fromId(Long id) {
        if (id == null) {
            return null;
        }
        MaterialsWarehouse materialsWarehouse = new MaterialsWarehouse();
        materialsWarehouse.setId(id);
        return materialsWarehouse;
    }
}
