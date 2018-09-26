package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.MaterialsWarehouse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MaterialsWarehouse entity.
 */
@Repository
public interface MaterialsWarehouseRepository 
	extends JpaRepository<MaterialsWarehouse, Long> {

}
