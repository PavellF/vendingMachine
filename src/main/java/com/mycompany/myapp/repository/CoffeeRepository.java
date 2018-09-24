package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Coffee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Coffee entity.
 */
@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

}
