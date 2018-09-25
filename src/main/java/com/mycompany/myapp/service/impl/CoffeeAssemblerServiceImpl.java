package com.mycompany.myapp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.CofeeMaterial;
import com.mycompany.myapp.domain.Coffee;
import com.mycompany.myapp.domain.MaterialsWarehouse;
import com.mycompany.myapp.repository.CofeeMaterialRepository;
import com.mycompany.myapp.repository.CoffeeRepository;
import com.mycompany.myapp.repository.MaterialsWarehouseRepository;
import com.mycompany.myapp.service.CoffeeAssemblerService;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import com.mycompany.myapp.service.mapper.CoffeeMapper;
import com.mycompany.myapp.web.rest.errors.NotEnoughIngredientsException;

/**
 * Basic implementation for CoffeeAssemblerService.
 **/
@Service
@SuppressWarnings("unused")
@Transactional
public class CoffeeAssemblerServiceImpl implements CoffeeAssemblerService {


	private final Logger log = LoggerFactory
			.getLogger(CoffeeAssemblerServiceImpl.class);
	
	private final CofeeMaterialRepository cofeeMaterialRepository;
	private final CoffeeRepository coffeeRepository;
	private final MaterialsWarehouseRepository materialsWarehouseRepository;
	private final CoffeeMapper coffeeMapper;
	
	public CoffeeAssemblerServiceImpl(
			CofeeMaterialRepository cofeeMaterialRepository,
			CoffeeRepository coffeeRepository,
			MaterialsWarehouseRepository materialsWarehouseRepository,
			CoffeeMapper coffeeMapper) {
		this.cofeeMaterialRepository = cofeeMaterialRepository;
		this.coffeeRepository = coffeeRepository;
		this.materialsWarehouseRepository = materialsWarehouseRepository;
		this.coffeeMapper = coffeeMapper;
	}



	/**
     * Assembles one coffee by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    public Optional<CoffeeDTO> assemblyOne(Long id) {
        log.debug("Request to assembly Coffee : {}", id);
        
        Optional<Coffee> coffee = coffeeRepository.findById(id);
       
        coffee.ifPresent((Coffee c) -> {
        	
        	Set<CofeeMaterial> materials = c.getCofees();
        	Map<String, Integer> notEnoughMaterials = new HashMap<>();
        	List<MaterialsWarehouse> toUpdate = new ArrayList<>();
        	
        	materials.stream().forEach((CofeeMaterial material) -> {
        		
        		MaterialsWarehouse warehouse = material.getMaterialsWarehouse();
        		Integer availableResouces = warehouse.getLeft();
        		Integer amountNeeded = material.getAmount();
        		Integer resourcesLeft = availableResouces - amountNeeded;
        		
        		if (resourcesLeft < 0) {
        			notEnoughMaterials.put(warehouse.getTitle(), Math.abs(resourcesLeft));
        		} else {
        			warehouse.setLeft(resourcesLeft);
        			toUpdate.add(warehouse);
        		}
        		
        	});
        	
        	if (notEnoughMaterials.isEmpty()) {
        		materialsWarehouseRepository.saveAll(toUpdate);
        	} else {
        		throw new NotEnoughIngredientsException(c.getTitle(), 
        				notEnoughMaterials);
        	}
        	
        });
    		   
    	return coffee.map(coffeeMapper::toDto);
    }

}
