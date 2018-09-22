package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CofeeMaterial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CofeeMaterial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CofeeMaterialRepository extends JpaRepository<CofeeMaterial, Long> {

}
