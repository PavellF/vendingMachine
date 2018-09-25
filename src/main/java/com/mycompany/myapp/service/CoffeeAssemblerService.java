package com.mycompany.myapp.service;

import java.util.Optional;

import com.mycompany.myapp.service.dto.CoffeeDTO;

/**
 * Service for coffee assembling.
 * */
public interface CoffeeAssemblerService {

	 /**
     * Returns coffee if there are all materials to assembly this, if coffee 
     * needs no materials to make it consider it as success anyway
     * @param id the id of the entity
     * @return the entity
     * @throws CoffeeAssemblyError if could nod assembly 
     * coffee (not enough materials).
     */
    Optional<CoffeeDTO> assemblyOne(Long id);
	
}
