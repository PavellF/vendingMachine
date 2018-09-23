package com.mycompany.myapp.service.mapper.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mycompany.myapp.domain.CofeeMaterial;
import com.mycompany.myapp.domain.Coffee;
import com.mycompany.myapp.domain.MaterialsWarehouse;
import com.mycompany.myapp.service.dto.CofeeMaterialDTO;
import com.mycompany.myapp.service.mapper.CofeeMaterialMapper;

/**
 * Basic implementation for {@code CofeeMaterialMapper}.
 * */
@Service
public class CofeeMaterialMapperImpl implements CofeeMaterialMapper {

	private static final Function<CofeeMaterialDTO, CofeeMaterial> TO_POJO = 
			(CofeeMaterialDTO cm) -> {
		CofeeMaterial toMap = new CofeeMaterial();
		toMap.setId(cm.getId());
		toMap.setAmount(cm.getAmount());
		
		Coffee coffee = new Coffee();
		coffee.setId(cm.getCoffeeId());
		toMap.setCoffee(coffee);
		
		MaterialsWarehouse materialsWarehouse = new MaterialsWarehouse();
		materialsWarehouse.setId(cm.getMaterialsWarehouseId());
		toMap.setMaterialsWarehouse(materialsWarehouse);
		
		return toMap;
	};
	
	private static final Function<CofeeMaterial, CofeeMaterialDTO> TO_DTO = 
			(CofeeMaterial cm) -> {
				CofeeMaterialDTO map = new CofeeMaterialDTO();
				map.setId(cm.getId());
				map.setAmount(cm.getAmount());
				
				if (cm.getCoffee() != null) {
					map.setCoffeeId(cm.getCoffee().getId());
				}
				
				if (cm.getMaterialsWarehouse() != null) {
					map.setCoffeeId(cm.getMaterialsWarehouse().getId());
				}
				
				return map;
	};
	
	@Override
	public List<CofeeMaterial> toEntity(List<CofeeMaterialDTO> dtoList) {
		if (dtoList == null) {
			return null;
		} 
		return dtoList.stream().map(TO_POJO).collect(Collectors.toList());
	}

	@Override
	public List<CofeeMaterialDTO> toDto(List<CofeeMaterial> entityList) {
		if (entityList == null) {
			return null;
		}
		return entityList.stream().map(TO_DTO).collect(Collectors.toList());
	}

	@Override
	public CofeeMaterialDTO toDto(CofeeMaterial cofeeMaterial) {
		return Optional.of(cofeeMaterial).map(TO_DTO).orElse(null);
	}

	@Override
	public CofeeMaterial toEntity(CofeeMaterialDTO cofeeMaterialDTO) {
		return Optional.of(cofeeMaterialDTO).map(TO_POJO).orElse(null);
	}

}
