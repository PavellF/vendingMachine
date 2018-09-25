package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.VendingMachineApp;
import com.mycompany.myapp.domain.CofeeMaterial;
import com.mycompany.myapp.domain.Coffee;
import com.mycompany.myapp.domain.MaterialsWarehouse;
import com.mycompany.myapp.repository.CoffeeRepository;
import com.mycompany.myapp.service.CoffeeAssemblerService;
import com.mycompany.myapp.service.CoffeeService;
import com.mycompany.myapp.service.MachineCleaningStateService;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import com.mycompany.myapp.service.mapper.CoffeeMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionAdvice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CoffeeResource REST controller.
 *
 * @see CoffeeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VendingMachineApp.class)
public class CoffeeResourceIntTest {

    private static final String DEFAULT_TITLE = "Empty";
    private static final String UPDATED_TITLE = "Empty updated";
    private static final String INVALID_TITLE = "EmptyEmptyEmptyEmptyEmpty"
    		+ "EmptyEmptyEmptyEmptyEmptyEmptyEmptyEmptyEmptyEmptyEmptyEmpty";
    
    private static final String CAPUCHINO_TITLE = "Capuchino";
    
    private static final Integer CAPUCHINO_MILK_ENOUGH = 2000;
    private static final Integer CAPUCHINO_MILK_NOT_ENOUGH = 2;
    private static final Integer CAPUCHINO_MILK_NEEDED = 100;
    
    private static final Integer CAPUCHINO_WATER_ENOUGH = 2000;
    private static final Integer CAPUCHINO_WATER_NOT_ENOUGH = 3;
    private static final Integer CAPUCHINO_WATER_NEEDED = 150;
    
    private static final Integer CAPUCHINO_BEANS_ENOUGH = 4000;
    private static final Integer CAPUCHINO_BEANS_NOT_ENOUGH = 4;
    private static final Integer CAPUCHINO_BEANS_NEEDED = 15;
    
    @Autowired
    private CoffeeRepository coffeeRepository;

    @Autowired
    private CoffeeMapper coffeeMapper;
    
    @Autowired
    private CoffeeService coffeeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionAdvice exceptionAdvice;
    
    @Autowired
    private CoffeeAssemblerService coffeeAssemblerService;
    
    @Autowired
    private MachineCleaningStateService machineCleaningStateService;

    @Autowired
    private EntityManager em;

    @Value("${counterMaxValue}")
	private Integer maxAmount;
    
    private MockMvc restCoffeeMockMvc;

    private Coffee coffee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoffeeResource coffeeResource = new CoffeeResource(coffeeService, 
        		machineCleaningStateService, coffeeAssemblerService);
        this.restCoffeeMockMvc = MockMvcBuilders.standaloneSetup(coffeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionAdvice)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coffee createEntity(EntityManager em) {
    	Coffee coffee = new Coffee();
    	coffee.setTitle(DEFAULT_TITLE);
    	return coffee;
    }
    
    private Coffee createCapuchinoWithAllIngredientsNeeded() {
    	return createCapuchino(em, CAPUCHINO_WATER_ENOUGH, CAPUCHINO_MILK_ENOUGH, CAPUCHINO_BEANS_ENOUGH);
    }
    
    private Coffee createCapuchinoWithNotEnoughInredients() {
    	return createCapuchino(em, CAPUCHINO_WATER_NOT_ENOUGH, CAPUCHINO_MILK_NOT_ENOUGH, CAPUCHINO_BEANS_NOT_ENOUGH);
    }
    
    private static Coffee createCapuchino(EntityManager em, int waterLeft, int milkLeft, int beansLeft) {
    	Coffee coffee = new Coffee();
    	coffee.setTitle(CAPUCHINO_TITLE);
    	em.persist(coffee);
    	
    	MaterialsWarehouse water = new MaterialsWarehouse();
    	water.setLeft(waterLeft);
    	water.setMaxAmount(9000);
    	water.setTitle("Water");
    	em.persist(water);
    	
    	MaterialsWarehouse milk = new MaterialsWarehouse();
    	milk.setLeft(milkLeft);
    	milk.setMaxAmount(9000);
    	milk.setTitle("Milk");
    	em.persist(milk);
    	
    	MaterialsWarehouse coffeeBeans = new MaterialsWarehouse();
    	coffeeBeans.setLeft(beansLeft);
    	coffeeBeans.setMaxAmount(9000);
    	coffeeBeans.setTitle("Coffee beans");
    	em.persist(coffeeBeans);
    	
    	CofeeMaterial waterMaterial = new CofeeMaterial();
    	waterMaterial.setAmount(CAPUCHINO_WATER_NEEDED);
    	waterMaterial.setCoffee(coffee);
    	waterMaterial.setMaterialsWarehouse(water);
    	em.persist(waterMaterial);
    	
    	CofeeMaterial milkMaterial = new CofeeMaterial();
    	milkMaterial.setAmount(CAPUCHINO_MILK_NEEDED);
    	milkMaterial.setCoffee(coffee);
    	milkMaterial.setMaterialsWarehouse(milk);
    	em.persist(milkMaterial);
    	
    	CofeeMaterial coffeeBeansMaterial = new CofeeMaterial();
    	coffeeBeansMaterial.setAmount(CAPUCHINO_BEANS_NEEDED);
    	coffeeBeansMaterial.setCoffee(coffee);
    	coffeeBeansMaterial.setMaterialsWarehouse(coffeeBeans);
    	em.persist(coffeeBeansMaterial);
    	
    	em.clear();
    	return coffee;
    }

    @Before
    public void initTest() {
        coffee = createEntity(em);
    }

    @Test
    @Transactional
    public void createCoffee() throws Exception {
        int databaseSizeBeforeCreate = coffeeRepository.findAll().size();

        // Create the Coffee
        CoffeeDTO coffeeDTO = coffeeMapper.toDto(coffee);
        restCoffeeMockMvc.perform(post("/api/coffees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coffeeDTO)))
            .andExpect(status().isCreated());

        // Validate the Coffee in the database
        List<Coffee> coffeeList = coffeeRepository.findAll();
        assertThat(coffeeList).hasSize(databaseSizeBeforeCreate + 1);
        Coffee testCoffee = coffeeList.get(coffeeList.size() - 1);
        assertThat(testCoffee.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createCoffeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = coffeeRepository.findAll().size();

        // Create the Coffee with an existing ID
        coffee.setId(1L);
        CoffeeDTO coffeeDTO = coffeeMapper.toDto(coffee);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoffeeMockMvc.perform(post("/api/coffees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coffeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coffee in the database
        List<Coffee> coffeeList = coffeeRepository.findAll();
        assertThat(coffeeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCoffees() throws Exception {
        // Initialize the database
        coffeeRepository.saveAndFlush(coffee);

        // Get all the coffeeList
        restCoffeeMockMvc.perform(get("/api/coffees?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coffee.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    
    @Test
    @Transactional
    public void assemblyCoffeeThatNeedsNothingToMakeIt() throws Exception {
        // Initialize the database
        coffeeRepository.saveAndFlush(coffee);

        // Get the coffee
        restCoffeeMockMvc.perform(get("/api/coffees/{id}", coffee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coffee.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }
    
    @Test
    @Transactional
    public void assemblyCapuchino() throws Exception {
        // Initialize the database
        Coffee coffee = createCapuchinoWithAllIngredientsNeeded();
        // Get the coffee
        restCoffeeMockMvc.perform(get("/api/coffees/{id}", coffee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coffee.getId().intValue()))
            .andExpect(jsonPath("$.title").value("Capuchino".toString()));
        
        assertThat(coffeeRepository.findById(coffee.getId())
        		.get()
        		.getCofees()
        		.stream()
        		.map(CofeeMaterial::getMaterialsWarehouse)
        		.map(MaterialsWarehouse::getLeft)
        		.collect(Collectors.toList())).contains(
        				CAPUCHINO_MILK_ENOUGH - CAPUCHINO_MILK_NEEDED, 
        				CAPUCHINO_WATER_ENOUGH - CAPUCHINO_WATER_NEEDED, 
        				CAPUCHINO_BEANS_ENOUGH - CAPUCHINO_BEANS_NEEDED);
        
    }
    
    @Test
    @Transactional
    public void shouldNotAssemblyCapuchino() throws Exception {
        // Initialize the database
        Coffee coffee = createCapuchinoWithNotEnoughInredients();
        // Get the coffee
        restCoffeeMockMvc.perform(get("/api/coffees/{id}", coffee.getId()))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.detail").isString());
        
        assertThat(coffeeRepository.findById(coffee.getId())
        		.get()
        		.getCofees()
        		.stream()
        		.map(CofeeMaterial::getMaterialsWarehouse)
        		.map(MaterialsWarehouse::getLeft)
        		.collect(Collectors.toList())).contains(
        				CAPUCHINO_MILK_NOT_ENOUGH, 
        				CAPUCHINO_WATER_NOT_ENOUGH, 
        				CAPUCHINO_BEANS_NOT_ENOUGH);
        
    }
    
    @Test
    @Transactional
    public void machineShouldBeRecharged() throws Exception {
        // Initialize the database
        Coffee coffee = createCapuchinoWithAllIngredientsNeeded();
        // Get the coffee
        
        for (int i = 0; i < maxAmount; i++) {
        	restCoffeeMockMvc.perform(get("/api/coffees/{id}", coffee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(coffee.getId().intValue()))
            .andExpect(jsonPath("$.title").value("Capuchino".toString()));
        	
            assertThat(coffeeRepository.findById(coffee.getId())
            		.get()
            		.getCofees()
            		.stream()
            		.map(CofeeMaterial::getMaterialsWarehouse)
            		.map(MaterialsWarehouse::getLeft)
            		.collect(Collectors.toList())).contains(
            				CAPUCHINO_MILK_ENOUGH - CAPUCHINO_MILK_NEEDED * (i + 1), 
            				CAPUCHINO_WATER_ENOUGH - CAPUCHINO_WATER_NEEDED * (i + 1), 
            				CAPUCHINO_BEANS_ENOUGH - CAPUCHINO_BEANS_NEEDED * (i + 1));
        }
        
        restCoffeeMockMvc.perform(get("/api/coffees/{id}", coffee.getId()))
        .andExpect(status().isInternalServerError());
        
        restCoffeeMockMvc.perform(put("/api/coffees/clean"))
        .andExpect(status().isNoContent());
    }

    @Test
    @Transactional
    public void getNonExistingCoffee() throws Exception {
        // Get the coffee
        restCoffeeMockMvc.perform(get("/api/coffees/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCoffee() throws Exception {
        // Initialize the database
        coffeeRepository.saveAndFlush(coffee);

        int databaseSizeBeforeUpdate = coffeeRepository.findAll().size();

        // Update the coffee
        Coffee updatedCoffee = coffeeRepository.findById(coffee.getId()).get();
        // Disconnect from session so that the updates on updatedCoffee are not directly saved in db
        em.detach(updatedCoffee);
        updatedCoffee.title(UPDATED_TITLE);
        CoffeeDTO coffeeDTO = coffeeMapper.toDto(updatedCoffee);

        restCoffeeMockMvc.perform(put("/api/coffees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coffeeDTO)))
            .andExpect(status().isOk());
       
        // Validate the Coffee in the database
        List<Coffee> coffeeList = coffeeRepository.findAll();
        assertThat(coffeeList).hasSize(databaseSizeBeforeUpdate);
        Coffee testCoffee = coffeeList.get(coffeeList.size() - 1);
        assertThat(testCoffee.getTitle()).isEqualTo(UPDATED_TITLE);
    }
    
    @Test
    @Transactional
    public void shouldNotUpdateInvalidCoffee() throws Exception {
        // Initialize the database
        coffeeRepository.saveAndFlush(coffee);

        int databaseSizeBeforeUpdate = coffeeRepository.findAll().size();

        // Update the coffee
        Coffee updatedCoffee = coffeeRepository.findById(coffee.getId()).get();
        // Disconnect from session so that the updates on updatedCoffee are not directly saved in db
        em.detach(updatedCoffee);
        updatedCoffee.title(INVALID_TITLE);
        CoffeeDTO coffeeDTO = coffeeMapper.toDto(updatedCoffee);

        restCoffeeMockMvc.perform(put("/api/coffees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coffeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coffee in the database
        List<Coffee> coffeeList = coffeeRepository.findAll();
        assertThat(coffeeList).hasSize(databaseSizeBeforeUpdate);
        Coffee testCoffee = coffeeList.get(coffeeList.size() - 1);
        assertThat(testCoffee.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingCoffee() throws Exception {
        int databaseSizeBeforeUpdate = coffeeRepository.findAll().size();

        // Create the Coffee
        CoffeeDTO coffeeDTO = coffeeMapper.toDto(coffee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoffeeMockMvc.perform(put("/api/coffees")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(coffeeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Coffee in the database
        List<Coffee> coffeeList = coffeeRepository.findAll();
        assertThat(coffeeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCoffee() throws Exception {
        // Initialize the database
        coffeeRepository.saveAndFlush(coffee);

        int databaseSizeBeforeDelete = coffeeRepository.findAll().size();

        // Get the coffee
        restCoffeeMockMvc.perform(delete("/api/coffees/{id}", coffee.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Coffee> coffeeList = coffeeRepository.findAll();
        assertThat(coffeeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Coffee.class);
        Coffee coffee1 = new Coffee();
        coffee1.setId(1L);
        Coffee coffee2 = new Coffee();
        coffee2.setId(coffee1.getId());
        assertThat(coffee1).isEqualTo(coffee2);
        coffee2.setId(2L);
        assertThat(coffee1).isNotEqualTo(coffee2);
        coffee1.setId(null);
        assertThat(coffee1).isNotEqualTo(coffee2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoffeeDTO.class);
        CoffeeDTO coffeeDTO1 = new CoffeeDTO();
        coffeeDTO1.setId(1L);
        CoffeeDTO coffeeDTO2 = new CoffeeDTO();
        assertThat(coffeeDTO1).isNotEqualTo(coffeeDTO2);
        coffeeDTO2.setId(coffeeDTO1.getId());
        assertThat(coffeeDTO1).isEqualTo(coffeeDTO2);
        coffeeDTO2.setId(2L);
        assertThat(coffeeDTO1).isNotEqualTo(coffeeDTO2);
        coffeeDTO1.setId(null);
        assertThat(coffeeDTO1).isNotEqualTo(coffeeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(coffeeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(coffeeMapper.fromId(null)).isNull();
    }
}
