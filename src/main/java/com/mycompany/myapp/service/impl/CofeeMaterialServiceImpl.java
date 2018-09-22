package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CofeeMaterialService;
import com.mycompany.myapp.domain.CofeeMaterial;
import com.mycompany.myapp.repository.CofeeMaterialRepository;
import com.mycompany.myapp.service.dto.CofeeMaterialDTO;
import com.mycompany.myapp.service.mapper.CofeeMaterialMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing CofeeMaterial.
 */
@Service
@Transactional
public class CofeeMaterialServiceImpl implements CofeeMaterialService {

    private final Logger log = LoggerFactory.getLogger(CofeeMaterialServiceImpl.class);

    private final CofeeMaterialRepository cofeeMaterialRepository;

    private final CofeeMaterialMapper cofeeMaterialMapper;

    public CofeeMaterialServiceImpl(CofeeMaterialRepository cofeeMaterialRepository, CofeeMaterialMapper cofeeMaterialMapper) {
        this.cofeeMaterialRepository = cofeeMaterialRepository;
        this.cofeeMaterialMapper = cofeeMaterialMapper;
    }

    /**
     * Save a cofeeMaterial.
     *
     * @param cofeeMaterialDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CofeeMaterialDTO save(CofeeMaterialDTO cofeeMaterialDTO) {
        log.debug("Request to save CofeeMaterial : {}", cofeeMaterialDTO);
        CofeeMaterial cofeeMaterial = cofeeMaterialMapper.toEntity(cofeeMaterialDTO);
        cofeeMaterial = cofeeMaterialRepository.save(cofeeMaterial);
        return cofeeMaterialMapper.toDto(cofeeMaterial);
    }

    /**
     * Get all the cofeeMaterials.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CofeeMaterialDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CofeeMaterials");
        return cofeeMaterialRepository.findAll(pageable)
            .map(cofeeMaterialMapper::toDto);
    }


    /**
     * Get one cofeeMaterial by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CofeeMaterialDTO> findOne(Long id) {
        log.debug("Request to get CofeeMaterial : {}", id);
        return cofeeMaterialRepository.findById(id)
            .map(cofeeMaterialMapper::toDto);
    }

    /**
     * Delete the cofeeMaterial by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CofeeMaterial : {}", id);
        cofeeMaterialRepository.deleteById(id);
    }
}
