package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.CoffeeAssemblerService;
import com.mycompany.myapp.service.CoffeeService;
import com.mycompany.myapp.service.MachineCleaningStateService;
import com.mycompany.myapp.web.rest.errors.Error;
import com.mycompany.myapp.web.rest.util.HeaderUtil;
import com.mycompany.myapp.web.rest.util.PaginationUtil;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import com.mycompany.myapp.web.rest.validators.CreateCoffeeValidator;
import com.mycompany.myapp.web.rest.validators.UpdateCoffeeValidator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * REST controller for managing Coffee.
 */
@RestController
@RequestMapping("/api")
public class CoffeeResource {

    private final Logger log = LoggerFactory.getLogger(CoffeeResource.class);

    private static final String ENTITY_NAME = "coffee";

    private final CoffeeService coffeeService;
    
    private final MachineCleaningStateService mcss;
    
    private final CoffeeAssemblerService coffeeAssemblerService;

    public CoffeeResource(CoffeeService coffeeService,
			MachineCleaningStateService mcss,
			CoffeeAssemblerService coffeeAssemblerService) {
		this.coffeeService = coffeeService;
		this.mcss = mcss;
		this.coffeeAssemblerService = coffeeAssemblerService;
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
    @ApiOperation(value = "Creates new type of coffee", notes = "Id field must be null, title field is required.")
    public ResponseEntity<CoffeeDTO> createCoffee(
    		@RequestBody @Valid CoffeeDTO coffeeDTO, BindingResult errors) throws URISyntaxException {
    	
        log.debug("REST request to save Coffee : {}", coffeeDTO);
        new CreateCoffeeValidator().validate(coffeeDTO, errors);

        if (errors.hasErrors()) {
            throw Error.validationError("/api/coffees", errors);
        }

        CoffeeDTO result = coffeeService.save(coffeeDTO);
        return ResponseEntity.created(new URI("/api/coffees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(
            		ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /coffees : Updates an existing coffee.
     *
     * @param coffeeDTO the coffeeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the 
     * updated coffeeDTO, or with status 400 (Bad Request) if the coffeeDTO 
     * is not valid, or with status 500 (Internal Server Error) if the coffeeDTO 
     * couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/coffees")
    @ApiOperation(value = "Updates coffee title", notes = "Id field must not be null.")
    public ResponseEntity<CoffeeDTO> updateCoffee(
    		@RequestBody @Valid CoffeeDTO coffeeDTO, BindingResult errors) throws URISyntaxException {
    	
        log.debug("REST request to update Coffee : {}", coffeeDTO);
        new UpdateCoffeeValidator(coffeeService).validate(coffeeDTO, errors);

        if (errors.hasErrors()) {
        	throw Error.validationError("/api/coffees", errors);
        }

        CoffeeDTO result = coffeeService.save(coffeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(
            		ENTITY_NAME, coffeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /coffees : get all the available coffees (not assembles them).
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list 
     * of coffees in body
     */
    @GetMapping("/coffees")
    @ApiOperation(value = "Get (not make) list of all available coffees")
    public ResponseEntity<List<CoffeeDTO>> getAllCoffees(Pageable pageable) {
        log.debug("REST request to get a page of Coffees");
        Page<CoffeeDTO> page = coffeeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/coffees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /coffees/:id : get the assembled coffee.
     *
     * @param id the id of the coffeeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the 
     * coffeeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/coffees/{id}")
    @ApiOperation(value = "Make coffee by its id, if there are enough materials available")
    public ResponseEntity<CoffeeDTO> getCoffee(@PathVariable Long id) {
        log.debug("REST request to make Coffee : {}", id);
        
        if(mcss.increaseCounter()) {
        	return coffeeAssemblerService.assemblyOne(id)
        			.map(coffee -> ResponseEntity.ok(coffee))
        			.orElse(ResponseEntity.notFound().build());
        } 
        	
        throw Error.builder()
    		.withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    		.withCode("cleaning")
    		.withDetail("Please clean machine before you can make coffee.")
    		.withTitle("Cleaning needed")
    		.withType(URI.create("/api/coffees/" + id))
    		.build();
    }

    /**
     * DELETE  /coffees/:id : delete the "id" coffee.
     *
     * @param id the id of the coffeeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/coffees/{id}")
    @ApiOperation(value = "Delete coffee by its id")
    public ResponseEntity<Void> deleteCoffee(@PathVariable Long id) {
        log.debug("REST request to delete Coffee : {}", id);
        coffeeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * POST  /coffees/clean : Cleans coffee machine
     *
     * @return the empty ResponseEntity with status 204 (No Content), 
     * or with status 400 (Bad Request) if machine can not be cleaned.
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/coffees/clean")
    @ApiOperation(value = "Performs cleaning of machine", notes = "If cleaning can not be performed " +
        "returns bad request status code")
    public ResponseEntity<?> clean() throws URISyntaxException {
    	log.debug("REST request to recharge coffee machine");
        return mcss.clean() 
        		? ResponseEntity.noContent().build()
        		: ResponseEntity.badRequest().build();
    }
}
