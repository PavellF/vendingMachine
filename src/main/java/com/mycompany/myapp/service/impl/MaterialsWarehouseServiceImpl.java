package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.MaterialsWarehouseService;
import com.mycompany.myapp.domain.MaterialsWarehouse;
import com.mycompany.myapp.repository.MaterialsWarehouseRepository;
import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;
import com.mycompany.myapp.service.mapper.MaterialsWarehouseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing MaterialsWarehouse.
 */
@Service
@Transactional
public class MaterialsWarehouseServiceImpl implements MaterialsWarehouseService {

    private final Logger log = LoggerFactory.getLogger(MaterialsWarehouseServiceImpl.class);

    private final MaterialsWarehouseRepository materialsWarehouseRepository;

    private final MaterialsWarehouseMapper materialsWarehouseMapper;

    public MaterialsWarehouseServiceImpl(MaterialsWarehouseRepository materialsWarehouseRepository, MaterialsWarehouseMapper materialsWarehouseMapper) {
        this.materialsWarehouseRepository = materialsWarehouseRepository;
        this.materialsWarehouseMapper = materialsWarehouseMapper;
    }

    /**
     * Save a materialsWarehouse.
     *
     * @param materialsWarehouseDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public MaterialsWarehouseDTO save(MaterialsWarehouseDTO materialsWarehouseDTO) {
        log.debug("Request to save MaterialsWarehouse : {}", materialsWarehouseDTO);
        MaterialsWarehouse materialsWarehouse = materialsWarehouseMapper.toEntity(materialsWarehouseDTO);
        materialsWarehouse = materialsWarehouseRepository.save(materialsWarehouse);
        return materialsWarehouseMapper.toDto(materialsWarehouse);
    }

    /**
     * Get all the materialsWarehouses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MaterialsWarehouseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MaterialsWarehouses");
        return materialsWarehouseRepository.findAll(pageable)
            .map(materialsWarehouseMapper::toDto);
    }


    /**
     * Get one materialsWarehouse by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<MaterialsWarehouseDTO> findOne(Long id) {
        log.debug("Request to get MaterialsWarehouse : {}", id);
        return materialsWarehouseRepository.findById(id)
            .map(materialsWarehouseMapper::toDto);
    }

    /**
     * Delete the materialsWarehouse by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete MaterialsWarehouse : {}", id);
        materialsWarehouseRepository.deleteById(id);
    }
}
