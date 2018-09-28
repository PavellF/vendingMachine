package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.MaterialsWarehouseService;
import com.mycompany.myapp.web.rest.errors.Error;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;
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

import javax.validation.Valid;

/**
 * REST controller for managing MaterialsWarehouse.
 */
@RestController
@RequestMapping("/api")
public class MaterialsWarehouseResource {

    private final Logger log = LoggerFactory
    		.getLogger(MaterialsWarehouseResource.class);

    private static final String ENTITY_NAME = "materialsWarehouse";

    private final MaterialsWarehouseService materialsWarehouseService;

    public MaterialsWarehouseResource(
    		MaterialsWarehouseService materialsWarehouseService) {
        this.materialsWarehouseService = materialsWarehouseService;
    }

    /**
     * POST  /materials-warehouses : Create a new materialsWarehouse.
     *
     * @param materialsWarehouseDTO the materialsWarehouseDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the 
     * new materialsWarehouseDTO, or with status 400 (Bad Request) if the 
     * materialsWarehouse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/materials-warehouses")
    public ResponseEntity<MaterialsWarehouseDTO> createMaterialsWarehouse(
    		@RequestBody @Valid MaterialsWarehouseDTO materialsWarehouseDTO) 
    				throws URISyntaxException {
        log.debug("REST request to save MaterialsWarehouse : {}", 
        		materialsWarehouseDTO);
        
        if (materialsWarehouseDTO.getId() != null) {
        	throw Error.builder()
    		.withStatus(HttpStatus.BAD_REQUEST)
    		.withCode("idexists")
    		.withDetail("A new materialsWarehouse cannot already have an ID")
    		.withTitle(ENTITY_NAME)
    		.withType(URI.create("/api/materials-warehouses"))
    		.build();
        }
        
        if (materialsWarehouseDTO.getLeft() > materialsWarehouseDTO.getMaxAmount()) {
        	throw Error.builder()
    		.withStatus(HttpStatus.BAD_REQUEST)
    		.withCode("overflow")
    		.withDetail("Amount left cannot be greater than max amount of material.")
    		.withTitle(ENTITY_NAME)
    		.withType(URI.create("/api/materials-warehouses"))
    		.build();
        }
        
        MaterialsWarehouseDTO result = materialsWarehouseService
        		.save(materialsWarehouseDTO);
        
        return ResponseEntity
        		.created(new URI("/api/materials-warehouses/" + result.getId()))
        		.headers(HeaderUtil.createEntityCreationAlert(
        				ENTITY_NAME, 
        				result.getId().toString()))
        		.body(result);
    }

    /**
     * PUT  /materials-warehouses : Updates an existing materialsWarehouse.
     *
     * @param materialsWarehouseDTO the materialsWarehouseDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated 
     * materialsWarehouseDTO, or with status 400 (Bad Request) if the 
     * materialsWarehouseDTO is not valid,or with status 500 
     * (Internal Server Error) if the materialsWarehouseDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/materials-warehouses")
    public ResponseEntity<MaterialsWarehouseDTO> updateMaterialsWarehouse(
    		@RequestBody @Valid MaterialsWarehouseDTO materialsWarehouseDTO) 
    				throws URISyntaxException {
    	
        log.debug("REST request to update MaterialsWarehouse : {}", 
        		materialsWarehouseDTO);
        
        if (materialsWarehouseDTO.getId() == null) {
        	throw Error.builder()
    		.withStatus(HttpStatus.BAD_REQUEST)
    		.withCode("idnull")
    		.withDetail("Invalid id")
    		.withTitle(ENTITY_NAME)
    		.withType(URI.create("/api/materials-warehouses"))
    		.build();
        }
        
        if (materialsWarehouseDTO.getLeft() > materialsWarehouseDTO.getMaxAmount()) {
        	throw Error.builder()
    		.withStatus(HttpStatus.BAD_REQUEST)
    		.withCode("overflow")
    		.withDetail("Amount left cannot be greater than max amount of material.")
    		.withTitle(ENTITY_NAME)
    		.withType(URI.create("/api/materials-warehouses"))
    		.build();
        }
        
        MaterialsWarehouseDTO result = materialsWarehouseService
        		.save(materialsWarehouseDTO);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(
            		ENTITY_NAME, 
            		materialsWarehouseDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /materials-warehouses : get all the materialsWarehouses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of 
     * materialsWarehouses in body
     */
    @GetMapping("/materials-warehouses")
    public ResponseEntity<List<MaterialsWarehouseDTO>> getAllMaterialsWarehouses(Pageable pageable) {
    	
        log.debug("REST request to get a page of MaterialsWarehouses");
        Page<MaterialsWarehouseDTO> page = materialsWarehouseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil
        		.generatePaginationHttpHeaders(page, "/api/materials-warehouses");
        
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /materials-warehouses/:id : get the "id" materialsWarehouse.
     *
     * @param id the id of the materialsWarehouseDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the 
     * materialsWarehouseDTO, or with status 404 (Not Found)
     */
    @GetMapping("/materials-warehouses/{id}")
    public ResponseEntity<MaterialsWarehouseDTO> getMaterialsWarehouse(
    		@PathVariable Long id) {
        log.debug("REST request to get MaterialsWarehouse : {}", id);
        
        return materialsWarehouseService.findOne(id)
        		.map(m -> ResponseEntity.ok(m))
        		.orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE  /materials-warehouses/:id : delete the "id" materialsWarehouse.
     *
     * @param id the id of the materialsWarehouseDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/materials-warehouses/{id}")
    public ResponseEntity<Void> deleteMaterialsWarehouse(@PathVariable Long id) {
        log.debug("REST request to delete MaterialsWarehouse : {}", id);
        materialsWarehouseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil
        		.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
