package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.VendingMachineApp;

import com.mycompany.myapp.domain.Coffee;
import com.mycompany.myapp.repository.CoffeeRepository;
import com.mycompany.myapp.service.CoffeeService;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import com.mycompany.myapp.service.mapper.CoffeeMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

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
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCoffeeMockMvc;

    private Coffee coffee;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CoffeeResource coffeeResource = new CoffeeResource(coffeeService);
        this.restCoffeeMockMvc = MockMvcBuilders.standaloneSetup(coffeeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
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
        Coffee coffee = new Coffee()
            .title(DEFAULT_TITLE);
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
    public void getCoffee() throws Exception {
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
        updatedCoffee
            .title(UPDATED_TITLE);
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
