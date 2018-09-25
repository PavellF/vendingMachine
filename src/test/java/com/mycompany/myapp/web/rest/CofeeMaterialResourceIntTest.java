package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.VendingMachineApp;

import com.mycompany.myapp.domain.CofeeMaterial;
import com.mycompany.myapp.domain.Coffee;
import com.mycompany.myapp.domain.MaterialsWarehouse;
import com.mycompany.myapp.repository.CofeeMaterialRepository;
import com.mycompany.myapp.service.CofeeMaterialService;
import com.mycompany.myapp.service.dto.CofeeMaterialDTO;
import com.mycompany.myapp.service.mapper.CofeeMaterialMapper;
import com.mycompany.myapp.web.rest.errors.ExceptionAdvice;

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
 * Test class for the CofeeMaterialResource REST controller.
 *
 * @see CofeeMaterialResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VendingMachineApp.class)
public class CofeeMaterialResourceIntTest {

    private static final Integer DEFAULT_AMOUNT = 150;
    private static final Integer UPDATED_AMOUNT = 200;
    private static final Integer INVALID_AMOUNT = -600;

    @Autowired
    private CofeeMaterialRepository cofeeMaterialRepository;

    @Autowired
    private CofeeMaterialMapper cofeeMaterialMapper;
    
    @Autowired
    private CofeeMaterialService cofeeMaterialService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;
    
    @Autowired
    private ExceptionAdvice exceptionAdvice;

    @Autowired
    private EntityManager em;

    private MockMvc restCofeeMaterialMockMvc;

    private CofeeMaterial cofeeMaterial;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CofeeMaterialResource cofeeMaterialResource = new CofeeMaterialResource(cofeeMaterialService);
        this.restCofeeMaterialMockMvc = MockMvcBuilders.standaloneSetup(cofeeMaterialResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setControllerAdvice(exceptionAdvice).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CofeeMaterial createEntity(EntityManager em) {
    	
    	Coffee coffee = new Coffee();
    	coffee.setTitle("Capuchino");
    	em.persist(coffee);
    	
    	MaterialsWarehouse water = new MaterialsWarehouse();
    	water.setLeft(2000);
    	water.setMaxAmount(9000);
    	water.setTitle("Water");
    	em.persist(water);
    	
    	MaterialsWarehouse milk = new MaterialsWarehouse();
    	milk.setLeft(2000);
    	milk.setMaxAmount(9000);
    	milk.setTitle("Milk");
    	em.persist(milk);
    	
    	MaterialsWarehouse coffeeBeans = new MaterialsWarehouse();
    	coffeeBeans.setLeft(4000);
    	coffeeBeans.setMaxAmount(9000);
    	coffeeBeans.setTitle("Coffee beans");
    	em.persist(coffeeBeans);
    	
    	CofeeMaterial waterMaterial = new CofeeMaterial();
    	waterMaterial.setAmount(DEFAULT_AMOUNT);
    	waterMaterial.setCoffee(coffee);
    	waterMaterial.setMaterialsWarehouse(water);
    	
    	CofeeMaterial milkMaterial = new CofeeMaterial();
    	milkMaterial.setAmount(100);
    	milkMaterial.setCoffee(coffee);
    	milkMaterial.setMaterialsWarehouse(milk);
    	em.persist(milkMaterial);
    	
    	CofeeMaterial coffeeBeansMaterial = new CofeeMaterial();
    	coffeeBeansMaterial.setAmount(15);
    	coffeeBeansMaterial.setCoffee(coffee);
    	coffeeBeansMaterial.setMaterialsWarehouse(coffeeBeans);
    	em.persist(coffeeBeansMaterial);
    	
    	em.clear();
    	return waterMaterial;
    }

    @Before
    public void initTest() {
        cofeeMaterial = createEntity(em);
    }

    @Test
    @Transactional
    public void createCofeeMaterial() throws Exception {
        int databaseSizeBeforeCreate = cofeeMaterialRepository.findAll().size();

        // Create the CofeeMaterial
        CofeeMaterialDTO cofeeMaterialDTO = cofeeMaterialMapper.toDto(cofeeMaterial);
        restCofeeMaterialMockMvc.perform(post("/api/cofee-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cofeeMaterialDTO)))
            .andExpect(status().isCreated());

        // Validate the CofeeMaterial in the database
        List<CofeeMaterial> cofeeMaterialList = cofeeMaterialRepository.findAll();
        assertThat(cofeeMaterialList).hasSize(databaseSizeBeforeCreate + 1);
        CofeeMaterial testCofeeMaterial = cofeeMaterialList.get(cofeeMaterialList.size() - 1);
        assertThat(testCofeeMaterial.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createCofeeMaterialWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cofeeMaterialRepository.findAll().size();

        // Create the CofeeMaterial with an existing ID
        cofeeMaterial.setId(1L);
        CofeeMaterialDTO cofeeMaterialDTO = cofeeMaterialMapper.toDto(cofeeMaterial);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCofeeMaterialMockMvc.perform(post("/api/cofee-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cofeeMaterialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CofeeMaterial in the database
        List<CofeeMaterial> cofeeMaterialList = cofeeMaterialRepository.findAll();
        assertThat(cofeeMaterialList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCofeeMaterials() throws Exception {
        // Initialize the database
        cofeeMaterialRepository.saveAndFlush(cofeeMaterial);

        // Get all the cofeeMaterialList
        restCofeeMaterialMockMvc.perform(get("/api/cofee-materials?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cofeeMaterial.getId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)));
    }
    
    @Test
    @Transactional
    public void getCofeeMaterial() throws Exception {
        // Initialize the database
        cofeeMaterialRepository.saveAndFlush(cofeeMaterial);

        // Get the cofeeMaterial
        restCofeeMaterialMockMvc.perform(get("/api/cofee-materials/{id}", cofeeMaterial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cofeeMaterial.getId().intValue()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingCofeeMaterial() throws Exception {
        // Get the cofeeMaterial
        restCofeeMaterialMockMvc.perform(get("/api/cofee-materials/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCofeeMaterial() throws Exception {
        // Initialize the database
        cofeeMaterialRepository.saveAndFlush(cofeeMaterial);

        int databaseSizeBeforeUpdate = cofeeMaterialRepository.findAll().size();

        // Update the cofeeMaterial
        CofeeMaterial updatedCofeeMaterial = cofeeMaterialRepository.findById(cofeeMaterial.getId()).get();
        // Disconnect from session so that the updates on updatedCofeeMaterial are not directly saved in db
        em.detach(updatedCofeeMaterial);
        updatedCofeeMaterial.amount(UPDATED_AMOUNT);
        CofeeMaterialDTO cofeeMaterialDTO = cofeeMaterialMapper.toDto(updatedCofeeMaterial);

        restCofeeMaterialMockMvc.perform(put("/api/cofee-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cofeeMaterialDTO)))
            .andExpect(status().isOk());

        // Validate the CofeeMaterial in the database
        List<CofeeMaterial> cofeeMaterialList = cofeeMaterialRepository.findAll();
        
        assertThat(cofeeMaterialList).hasSize(databaseSizeBeforeUpdate);
        CofeeMaterial testCofeeMaterial = cofeeMaterialList.get(cofeeMaterialList.size() - 1);
        assertThat(testCofeeMaterial.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }
    
    @Test
    @Transactional
    public void updateCofeeMaterialWithInvalidAmountValue() throws Exception {
        // Initialize the database
        cofeeMaterialRepository.saveAndFlush(cofeeMaterial);

        int databaseSizeBeforeUpdate = cofeeMaterialRepository.findAll().size();

        // Update the cofeeMaterial
        CofeeMaterial updatedCofeeMaterial = cofeeMaterialRepository.findById(cofeeMaterial.getId()).get();
        // Disconnect from session so that the updates on updatedCofeeMaterial are not directly saved in db
        em.detach(updatedCofeeMaterial);
        updatedCofeeMaterial.amount(INVALID_AMOUNT);
        CofeeMaterialDTO cofeeMaterialDTO = cofeeMaterialMapper.toDto(updatedCofeeMaterial);

        restCofeeMaterialMockMvc.perform(put("/api/cofee-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cofeeMaterialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CofeeMaterial in the database
        List<CofeeMaterial> cofeeMaterialList = cofeeMaterialRepository.findAll();
        
        assertThat(cofeeMaterialList).hasSize(databaseSizeBeforeUpdate);
        CofeeMaterial testCofeeMaterial = cofeeMaterialList.get(cofeeMaterialList.size() - 1);
        assertThat(testCofeeMaterial.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingCofeeMaterial() throws Exception {
        int databaseSizeBeforeUpdate = cofeeMaterialRepository.findAll().size();

        // Create the CofeeMaterial
        CofeeMaterialDTO cofeeMaterialDTO = cofeeMaterialMapper.toDto(cofeeMaterial);

        // If the entity doesn't have an ID, it will not go
        restCofeeMaterialMockMvc.perform(put("/api/cofee-materials")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cofeeMaterialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CofeeMaterial in the database
        List<CofeeMaterial> cofeeMaterialList = cofeeMaterialRepository.findAll();
        assertThat(cofeeMaterialList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCofeeMaterial() throws Exception {
        // Initialize the database
        cofeeMaterialRepository.saveAndFlush(cofeeMaterial);

        int databaseSizeBeforeDelete = cofeeMaterialRepository.findAll().size();

        // Get the cofeeMaterial
        restCofeeMaterialMockMvc.perform(delete("/api/cofee-materials/{id}", cofeeMaterial.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CofeeMaterial> cofeeMaterialList = cofeeMaterialRepository.findAll();
        assertThat(cofeeMaterialList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
    	
    	TestUtil.equalsVerifier(CofeeMaterial.class);
        CofeeMaterial cofeeMaterial1 = new CofeeMaterial();
        cofeeMaterial1.setId(1L);
        CofeeMaterial cofeeMaterial2 = new CofeeMaterial();
        cofeeMaterial2.setId(cofeeMaterial1.getId());
        assertThat(cofeeMaterial1).isEqualTo(cofeeMaterial2);
        cofeeMaterial2.setId(2L);
        assertThat(cofeeMaterial1).isNotEqualTo(cofeeMaterial2);
        cofeeMaterial1.setId(null);
        assertThat(cofeeMaterial1).isNotEqualTo(cofeeMaterial2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CofeeMaterialDTO.class);
        CofeeMaterialDTO cofeeMaterialDTO1 = new CofeeMaterialDTO();
        cofeeMaterialDTO1.setId(1L);
        CofeeMaterialDTO cofeeMaterialDTO2 = new CofeeMaterialDTO();
        assertThat(cofeeMaterialDTO1).isNotEqualTo(cofeeMaterialDTO2);
        cofeeMaterialDTO2.setId(cofeeMaterialDTO1.getId());
        assertThat(cofeeMaterialDTO1).isEqualTo(cofeeMaterialDTO2);
        cofeeMaterialDTO2.setId(2L);
        assertThat(cofeeMaterialDTO1).isNotEqualTo(cofeeMaterialDTO2);
        cofeeMaterialDTO1.setId(null);
        assertThat(cofeeMaterialDTO1).isNotEqualTo(cofeeMaterialDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cofeeMaterialMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cofeeMaterialMapper.fromId(null)).isNull();
    }
}
