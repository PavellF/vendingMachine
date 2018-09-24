package com.mycompany.myapp.service.mapper.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mycompany.myapp.domain.Coffee;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import com.mycompany.myapp.service.mapper.CoffeeMapper;

/**
 * Basic implementation for {@code CoffeeMapper}.
 * */
@Service
public class CoffeeMapperImpl implements CoffeeMapper {

	private static final Function<Coffee, CoffeeDTO> TO_DTO = (Coffee c) -> {
		CoffeeDTO dto = new CoffeeDTO();
		dto.setId(c.getId());
		dto.setTitle(c.getTitle());
		return dto;
	};
	
	private static final Function<CoffeeDTO, Coffee> TO_ENTITY = 
			(CoffeeDTO dto) -> {
		Coffee c = new Coffee();
		c.setId(dto.getId());
		c.setTitle(c.getTitle());
		return c;
	};
	
	@Override
	public CoffeeDTO toDto(Coffee entity) {
		return Optional.of(entity).map(TO_DTO).orElseGet(null);
	}

	@Override
	public List<Coffee> toEntity(List<CoffeeDTO> dtoList) {
		if (dtoList == null) {
			return null;
		}
		return dtoList.stream().map(TO_ENTITY).collect(Collectors.toList());
	}

	@Override
	public List<CoffeeDTO> toDto(List<Coffee> entityList) {
		if (entityList == null) {
			return null;
		}
		return entityList.stream().map(TO_DTO).collect(Collectors.toList());
	}

	@Override
	public Coffee toEntity(CoffeeDTO coffeeDTO) {
		return Optional.of(coffeeDTO).map(TO_ENTITY).orElseGet(null);
	}

}
