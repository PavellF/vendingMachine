package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CofeeMaterial;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CofeeMaterial entity.
 */
@Repository
public interface CofeeMaterialRepository 
	extends JpaRepository<CofeeMaterial, Long> {

	@Query("SELECT cm FROM CofeeMaterial AS cm WHERE cm.coffee = ?1")
	List<CofeeMaterial> findAllIngredientsForCoffee(Long id);
	
}
