package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CoffeeService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import io.github.jhipster.web.util.ResponseUtil;
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
import java.util.Optional;

/**
 * REST controller for managing Coffee.
 */
@RestController
@RequestMapping("/api")
public class CoffeeResource {

    private final Logger log = LoggerFactory.getLogger(CoffeeResource.class);

    private static final String ENTITY_NAME = "coffee";

    private final CoffeeService coffeeService;

    public CoffeeResource(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    /**
     * POST  /coffees : Create a new coffee.
     *
     * @param coffeeDTO the coffeeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body of 
     * the new coffeeDTO, or with status 400 (Bad Request) if the coffee has 
     * already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coffees")
    public ResponseEntity<CoffeeDTO> createCoffee(@RequestBody CoffeeDTO coffeeDTO) throws URISyntaxException {
        log.debug("REST request to save Coffee : {}", coffeeDTO);
        if (coffeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new coffee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CoffeeDTO result = coffeeService.save(coffeeDTO);
        return ResponseEntity.created(new URI("/api/coffees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coffees : Updates an existing coffee.
     *
     * @param coffeeDTO the coffeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated coffeeDTO,
     * or with status 400 (Bad Request) if the coffeeDTO is not valid,
     * or with status 500 (Internal Server Error) if the coffeeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coffees")
    @Timed
    public ResponseEntity<CoffeeDTO> updateCoffee(@RequestBody CoffeeDTO coffeeDTO) throws URISyntaxException {
        log.debug("REST request to update Coffee : {}", coffeeDTO);
        if (coffeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CoffeeDTO result = coffeeService.save(coffeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, coffeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coffees : get all the coffees.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of coffees in body
     */
    @GetMapping("/coffees")
    public ResponseEntity<List<CoffeeDTO>> getAllCoffees(Pageable pageable) {
        log.debug("REST request to get a page of Coffees");
        Page<CoffeeDTO> page = coffeeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coffees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coffees/:id : get the "id" coffee.
     *
     * @param id the id of the coffeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the coffeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coffees/{id}")
    public ResponseEntity<CoffeeDTO> getCoffee(@PathVariable Long id) {
        log.debug("REST request to get Coffee : {}", id);
        Optional<CoffeeDTO> coffeeDTO = coffeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(coffeeDTO);
    }

    /**
     * DELETE  /coffees/:id : delete the "id" coffee.
     *
     * @param id the id of the coffeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coffees/{id}")
    public ResponseEntity<Void> deleteCoffee(@PathVariable Long id) {
        log.debug("REST request to delete Coffee : {}", id);
        coffeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
