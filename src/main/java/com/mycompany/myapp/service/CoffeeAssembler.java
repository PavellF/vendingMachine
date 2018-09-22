package com.mycompany.myapp.service;

import java.util.Optional;

import com.mycompany.myapp.service.dto.CoffeeDTO;

public interface CoffeeAssembler {

	Optional<CoffeeDTO> assemblyCofee(Long id);
	
}
