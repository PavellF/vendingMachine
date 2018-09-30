package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CoffeeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


/**
 * Service Interface for managing Coffee.
 */
public interface CoffeeService {

    /**
     * Save a coffee.
     *
     * @param coffeeDTO the entity to save
     * @return the persisted entity
     */
    CoffeeDTO save(CoffeeDTO coffeeDTO);

    /**
     * Get all the available coffees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CoffeeDTO> findAll(Pageable pageable);

    /**
     * Delete the "id" coffee.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Tries to find coffee.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CoffeeDTO> findOne(Long id);
}
