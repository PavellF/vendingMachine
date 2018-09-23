package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CofeeMaterialService;
import com.mycompany.myapp.web.rest.errors.Error;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.CofeeMaterialDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;

/**
 * REST controller for managing CofeeMaterial.
 */
@RestController
@RequestMapping("/api")
public class CofeeMaterialResource {

    private final Logger log = LoggerFactory
    		.getLogger(CofeeMaterialResource.class);

    private static final String ENTITY_NAME = "cofeeMaterial";

    private final CofeeMaterialService cofeeMaterialService;

    public CofeeMaterialResource(CofeeMaterialService cofeeMaterialService) {
        this.cofeeMaterialService = cofeeMaterialService;
    }

    /**
     * POST  /cofee-materials : Create a new cofeeMaterial.
     *
     * @param cofeeMaterialDTO the cofeeMaterialDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the 
     * new cofeeMaterialDTO, or with status 400 (Bad Request) if the 
     * cofeeMaterial has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cofee-materials")
    public ResponseEntity<CofeeMaterialDTO> createCofeeMaterial(
    		@RequestBody CofeeMaterialDTO cofeeMaterialDTO) 
    				throws URISyntaxException {
    	
        log.debug("REST request to save CofeeMaterial : {}", cofeeMaterialDTO);
        
        if (cofeeMaterialDTO.getId() != null) {
        	throw Error.builder()
	        		.withStatus(HttpStatus.BAD_REQUEST)
	        		.withCode("idexists")
	        		.withDetail("A new cofeeMaterial already have an ID")
	        		.withTitle(ENTITY_NAME)
	        		.withType(URI.create("/api/cofee-materials/"))
	        		.build();
        }
        
        CofeeMaterialDTO result = cofeeMaterialService.save(cofeeMaterialDTO);
        
        return ResponseEntity
        		.created(URI.create("/api/cofee-materials/" + result.getId()))
        		.headers(HeaderUtil.createEntityCreationAlert(
        				ENTITY_NAME, result.getId().toString()))
        		.body(result);
    }

    /**
     * PUT  /cofee-materials : Updates an existing cofeeMaterial.
     *
     * @param cofeeMaterialDTO the cofeeMaterialDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated 
     * cofeeMaterialDTO, or with status 400 (Bad Request) if the 
     * cofeeMaterialDTO is not valid, or with status 500 (Internal Server Error)
     * if the cofeeMaterialDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cofee-materials")
    public ResponseEntity<CofeeMaterialDTO> updateCofeeMaterial(
    		@RequestBody CofeeMaterialDTO cofeeMaterialDTO) 
    				throws URISyntaxException {
    	
        log.debug("REST request to update CofeeMaterial : {}",cofeeMaterialDTO);
        
        if (cofeeMaterialDTO.getId() == null) {
        	throw Error.builder()
	    		.withStatus(HttpStatus.BAD_REQUEST)
	    		.withCode("idnull")
	    		.withDetail("Invalid id")
	    		.withTitle(ENTITY_NAME)
	    		.withType(URI.create("/api/cofee-materials/"))
	    		.build();
        }
        
        CofeeMaterialDTO result = cofeeMaterialService.save(cofeeMaterialDTO);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, 
            		cofeeMaterialDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cofee-materials : get all the cofeeMaterials.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of 
     * cofeeMaterials in body
     */
    @GetMapping("/cofee-materials")
    public ResponseEntity<List<CofeeMaterialDTO>> getAllCofeeMaterials(
    		Pageable pageable) {
    	
        log.debug("REST request to get a page of CofeeMaterials");
        
        Page<CofeeMaterialDTO> page = cofeeMaterialService.findAll(pageable);
        
        HttpHeaders headers = PaginationUtil
        		.generatePaginationHttpHeaders(page, "/api/cofee-materials");
        
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cofee-materials/:id : get the "id" cofeeMaterial.
     *
     * @param id the id of the cofeeMaterialDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the 
     * cofeeMaterialDTO, or with status 404 (Not Found)
     */
    @GetMapping("/cofee-materials/{id}")
    public ResponseEntity<CofeeMaterialDTO> getCofeeMaterial(
    		@PathVariable Long id) {
    	
        log.debug("REST request to get CofeeMaterial : {}", id);
        
        return cofeeMaterialService.findOne(id)
        		.map(dto -> ResponseEntity.ok(dto))
        		.orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE  /cofee-materials/:id : delete the "id" cofeeMaterial.
     *
     * @param id the id of the cofeeMaterialDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cofee-materials/{id}")
    public ResponseEntity<Void> deleteCofeeMaterial(@PathVariable Long id) {
        log.debug("REST request to delete CofeeMaterial : {}", id);
        cofeeMaterialService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil
        		.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
