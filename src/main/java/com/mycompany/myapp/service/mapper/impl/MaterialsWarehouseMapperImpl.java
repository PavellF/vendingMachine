package com.mycompany.myapp.service.mapper.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mycompany.myapp.domain.MaterialsWarehouse;
import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;
import com.mycompany.myapp.service.mapper.MaterialsWarehouseMapper;

@Service
public class MaterialsWarehouseMapperImpl implements MaterialsWarehouseMapper {

	private static final Function<MaterialsWarehouse, MaterialsWarehouseDTO> 
		TO_DTO = (MaterialsWarehouse e) -> {
			
		MaterialsWarehouseDTO dto = new MaterialsWarehouseDTO();
		dto.setId(e.getId());
		dto.setLeft(e.getLeft());
		dto.setMaxAmount(e.getMaxAmount());
		dto.setTitle(e.getTitle());
		return dto;
	};
	
	private static final Function<MaterialsWarehouseDTO, MaterialsWarehouse> 
		TO_ENTITY = (MaterialsWarehouseDTO e) -> {
		
		MaterialsWarehouse entity = new MaterialsWarehouse();
		entity.setId(e.getId());
		entity.setLeft(e.getLeft());
		entity.setMaxAmount(e.getMaxAmount());
		entity.setTitle(e.getTitle());
		return entity;
	};
	
	@Override
	public MaterialsWarehouseDTO toDto(MaterialsWarehouse entity) {
		return Optional.ofNullable(entity).map(TO_DTO).orElse(null);
	}

	@Override
	public List<MaterialsWarehouse> toEntity(
			List<MaterialsWarehouseDTO> dtoList) {
		if (dtoList == null) {
			return null;
		}
		return dtoList.stream().map(TO_ENTITY).collect(Collectors.toList());
	}

	@Override
	public List<MaterialsWarehouseDTO> toDto(
			List<MaterialsWarehouse> entityList) {
		if (entityList == null) {
			return null;
		}
		return entityList.stream().map(TO_DTO).collect(Collectors.toList());
	}

	@Override
	public MaterialsWarehouse toEntity(
			MaterialsWarehouseDTO materialsWarehouseDTO) {
		return Optional.ofNullable(materialsWarehouseDTO)
				.map(TO_ENTITY).orElse(null);
	}

}
