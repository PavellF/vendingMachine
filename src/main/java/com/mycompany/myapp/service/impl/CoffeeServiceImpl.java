package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.CoffeeService;
import com.mycompany.myapp.domain.Coffee;
import com.mycompany.myapp.repository.CoffeeRepository;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import com.mycompany.myapp.service.mapper.CoffeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Coffee.
 */
@Service
@Transactional
public class CoffeeServiceImpl implements CoffeeService {

    private final Logger log = LoggerFactory.getLogger(CoffeeServiceImpl.class);

    private final CoffeeRepository coffeeRepository;
    
    private final CoffeeMapper coffeeMapper;

    public CoffeeServiceImpl(CoffeeRepository coffeeRepository,
			CoffeeMapper coffeeMapper) {
		this.coffeeRepository = coffeeRepository;
		this.coffeeMapper = coffeeMapper;
	}

    /**
     * Get one coffee by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CoffeeDTO> findOne(Long id) {
        log.debug("Request to get Coffee : {}", id);
        return coffeeRepository.findById(id).map(coffeeMapper::toDto);
    }


    /**
     * Save a coffee.
     *
     * @param coffeeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CoffeeDTO save(CoffeeDTO coffeeDTO) {
        log.debug("Request to save Coffee : {}", coffeeDTO);
        Coffee coffee = coffeeMapper.toEntity(coffeeDTO);
        coffee = coffeeRepository.save(coffee);
        return coffeeMapper.toDto(coffee);
    }

    /**
     * Get all the coffees.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CoffeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Coffees");
        return coffeeRepository.findAll(pageable)
            .map(coffeeMapper::toDto);
    }


    /**
     * Delete the coffee by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Coffee : {}", id);
        coffeeRepository.deleteById(id);
    }

}
