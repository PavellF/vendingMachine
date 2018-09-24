package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;


/**
 * Mapper for the entity MaterialsWarehouse and its DTO MaterialsWarehouseDTO.
 */
public interface MaterialsWarehouseMapper 
	extends EntityMapper<MaterialsWarehouseDTO, MaterialsWarehouse> {


    default MaterialsWarehouse fromId(Long id) {
        if (id == null) {
            return null;
        }
        MaterialsWarehouse materialsWarehouse = new MaterialsWarehouse();
        materialsWarehouse.setId(id);
        return materialsWarehouse;
    }
}
