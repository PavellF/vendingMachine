package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.*;
import com.mycompany.myapp.service.dto.CofeeMaterialDTO;

/**
 * Mapper for the entity CofeeMaterial and its DTO CofeeMaterialDTO.
 */
public interface CofeeMaterialMapper extends EntityMapper<CofeeMaterialDTO, CofeeMaterial> {

    default CofeeMaterial fromId(Long id) {
        if (id == null) {
            return null;
        }
        CofeeMaterial cofeeMaterial = new CofeeMaterial();
        cofeeMaterial.setId(id);
        return cofeeMaterial;
    }
}
