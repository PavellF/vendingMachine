package com.mycompany.myapp.service;

import java.util.Optional;

import com.mycompany.myapp.service.dto.CoffeeDTO;

/**
 * Service for coffee assembling.
 * */
public interface CoffeeAssemblerService {

	 /**
     * Tries to assembly coffee.
     *
     * @param id the id of the entity
     * @return the entity
     * @throws CoffeeAssemblyError if could nod assembly coffee.
     */
    Optional<CoffeeDTO> assemblyOne(Long id);
	
}
