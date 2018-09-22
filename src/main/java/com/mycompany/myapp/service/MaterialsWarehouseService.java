package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing MaterialsWarehouse.
 */
public interface MaterialsWarehouseService {

    /**
     * Save a materialsWarehouse.
     *
     * @param materialsWarehouseDTO the entity to save
     * @return the persisted entity
     */
    MaterialsWarehouseDTO save(MaterialsWarehouseDTO materialsWarehouseDTO);

    /**
     * Get all the materialsWarehouses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<MaterialsWarehouseDTO> findAll(Pageable pageable);


    /**
     * Get the "id" materialsWarehouse.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<MaterialsWarehouseDTO> findOne(Long id);

    /**
     * Delete the "id" materialsWarehouse.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
