package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CofeeMaterialDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CofeeMaterial.
 */
public interface CofeeMaterialService {

	/**
     * Save a cofeeMaterial.
     *
     * @param cofeeMaterialDTO the entity to save
     * @return the persisted entity
     */
    CofeeMaterialDTO save(CofeeMaterialDTO cofeeMaterialDTO);

    /**
     * Get all the cofeeMaterials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CofeeMaterialDTO> findAll(Pageable pageable);


    /**
     * Get the "id" cofeeMaterial.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CofeeMaterialDTO> findOne(Long id);

    /**
     * Delete the "id" cofeeMaterial.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
