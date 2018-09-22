package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.VendingMachineApp;

import com.mycompany.myapp.domain.MaterialsWarehouse;
import com.mycompany.myapp.repository.MaterialsWarehouseRepository;
import com.mycompany.myapp.service.MaterialsWarehouseService;
import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;
import com.mycompany.myapp.service.mapper.MaterialsWarehouseMapper;
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
 * Test class for the MaterialsWarehouseResource REST controller.
 *
 * @see MaterialsWarehouseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VendingMachineApp.class)
public class MaterialsWarehouseResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_LEFT = 1;
    private static final Integer UPDATED_LEFT = 2;

    private static final Integer DEFAULT_MAX_AMOUNT = 1;
    private static final Integer UPDATED_MAX_AMOUNT = 2;

    @Autowired
    private MaterialsWarehouseRepository materialsWarehouseRepository;

    @Autowired
    private MaterialsWarehouseMapper materialsWarehouseMapper;
    
    @Autowired
    private MaterialsWarehouseService materialsWarehouseService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMaterialsWarehouseMockMvc;

    private MaterialsWarehouse materialsWarehouse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaterialsWarehouseResource materialsWarehouseResource = new MaterialsWarehouseResource(materialsWarehouseService);
        this.restMaterialsWarehouseMockMvc = MockMvcBuilders.standaloneSetup(materialsWarehouseResource)
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
    public static MaterialsWarehouse createEntity(EntityManager em) {
        MaterialsWarehouse materialsWarehouse = new MaterialsWarehouse()
            .title(DEFAULT_TITLE)
            .left(DEFAULT_LEFT)
            .maxAmount(DEFAULT_MAX_AMOUNT);
        return materialsWarehouse;
    }

    @Before
    public void initTest() {
        materialsWarehouse = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaterialsWarehouse() throws Exception {
        int databaseSizeBeforeCreate = materialsWarehouseRepository.findAll().size();

        // Create the MaterialsWarehouse
        MaterialsWarehouseDTO materialsWarehouseDTO = materialsWarehouseMapper.toDto(materialsWarehouse);
        restMaterialsWarehouseMockMvc.perform(post("/api/materials-warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialsWarehouseDTO)))
            .andExpect(status().isCreated());

        // Validate the MaterialsWarehouse in the database
        List<MaterialsWarehouse> materialsWarehouseList = materialsWarehouseRepository.findAll();
        assertThat(materialsWarehouseList).hasSize(databaseSizeBeforeCreate + 1);
        MaterialsWarehouse testMaterialsWarehouse = materialsWarehouseList.get(materialsWarehouseList.size() - 1);
        assertThat(testMaterialsWarehouse.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMaterialsWarehouse.getLeft()).isEqualTo(DEFAULT_LEFT);
        assertThat(testMaterialsWarehouse.getMaxAmount()).isEqualTo(DEFAULT_MAX_AMOUNT);
    }

    @Test
    @Transactional
    public void createMaterialsWarehouseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = materialsWarehouseRepository.findAll().size();

        // Create the MaterialsWarehouse with an existing ID
        materialsWarehouse.setId(1L);
        MaterialsWarehouseDTO materialsWarehouseDTO = materialsWarehouseMapper.toDto(materialsWarehouse);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaterialsWarehouseMockMvc.perform(post("/api/materials-warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialsWarehouseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialsWarehouse in the database
        List<MaterialsWarehouse> materialsWarehouseList = materialsWarehouseRepository.findAll();
        assertThat(materialsWarehouseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMaterialsWarehouses() throws Exception {
        // Initialize the database
        materialsWarehouseRepository.saveAndFlush(materialsWarehouse);

        // Get all the materialsWarehouseList
        restMaterialsWarehouseMockMvc.perform(get("/api/materials-warehouses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(materialsWarehouse.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].left").value(hasItem(DEFAULT_LEFT)))
            .andExpect(jsonPath("$.[*].maxAmount").value(hasItem(DEFAULT_MAX_AMOUNT)));
    }
    
    @Test
    @Transactional
    public void getMaterialsWarehouse() throws Exception {
        // Initialize the database
        materialsWarehouseRepository.saveAndFlush(materialsWarehouse);

        // Get the materialsWarehouse
        restMaterialsWarehouseMockMvc.perform(get("/api/materials-warehouses/{id}", materialsWarehouse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(materialsWarehouse.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.left").value(DEFAULT_LEFT))
            .andExpect(jsonPath("$.maxAmount").value(DEFAULT_MAX_AMOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingMaterialsWarehouse() throws Exception {
        // Get the materialsWarehouse
        restMaterialsWarehouseMockMvc.perform(get("/api/materials-warehouses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaterialsWarehouse() throws Exception {
        // Initialize the database
        materialsWarehouseRepository.saveAndFlush(materialsWarehouse);

        int databaseSizeBeforeUpdate = materialsWarehouseRepository.findAll().size();

        // Update the materialsWarehouse
        MaterialsWarehouse updatedMaterialsWarehouse = materialsWarehouseRepository.findById(materialsWarehouse.getId()).get();
        // Disconnect from session so that the updates on updatedMaterialsWarehouse are not directly saved in db
        em.detach(updatedMaterialsWarehouse);
        updatedMaterialsWarehouse
            .title(UPDATED_TITLE)
            .left(UPDATED_LEFT)
            .maxAmount(UPDATED_MAX_AMOUNT);
        MaterialsWarehouseDTO materialsWarehouseDTO = materialsWarehouseMapper.toDto(updatedMaterialsWarehouse);

        restMaterialsWarehouseMockMvc.perform(put("/api/materials-warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialsWarehouseDTO)))
            .andExpect(status().isOk());

        // Validate the MaterialsWarehouse in the database
        List<MaterialsWarehouse> materialsWarehouseList = materialsWarehouseRepository.findAll();
        assertThat(materialsWarehouseList).hasSize(databaseSizeBeforeUpdate);
        MaterialsWarehouse testMaterialsWarehouse = materialsWarehouseList.get(materialsWarehouseList.size() - 1);
        assertThat(testMaterialsWarehouse.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMaterialsWarehouse.getLeft()).isEqualTo(UPDATED_LEFT);
        assertThat(testMaterialsWarehouse.getMaxAmount()).isEqualTo(UPDATED_MAX_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingMaterialsWarehouse() throws Exception {
        int databaseSizeBeforeUpdate = materialsWarehouseRepository.findAll().size();

        // Create the MaterialsWarehouse
        MaterialsWarehouseDTO materialsWarehouseDTO = materialsWarehouseMapper.toDto(materialsWarehouse);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaterialsWarehouseMockMvc.perform(put("/api/materials-warehouses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(materialsWarehouseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaterialsWarehouse in the database
        List<MaterialsWarehouse> materialsWarehouseList = materialsWarehouseRepository.findAll();
        assertThat(materialsWarehouseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaterialsWarehouse() throws Exception {
        // Initialize the database
        materialsWarehouseRepository.saveAndFlush(materialsWarehouse);

        int databaseSizeBeforeDelete = materialsWarehouseRepository.findAll().size();

        // Get the materialsWarehouse
        restMaterialsWarehouseMockMvc.perform(delete("/api/materials-warehouses/{id}", materialsWarehouse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MaterialsWarehouse> materialsWarehouseList = materialsWarehouseRepository.findAll();
        assertThat(materialsWarehouseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialsWarehouse.class);
        MaterialsWarehouse materialsWarehouse1 = new MaterialsWarehouse();
        materialsWarehouse1.setId(1L);
        MaterialsWarehouse materialsWarehouse2 = new MaterialsWarehouse();
        materialsWarehouse2.setId(materialsWarehouse1.getId());
        assertThat(materialsWarehouse1).isEqualTo(materialsWarehouse2);
        materialsWarehouse2.setId(2L);
        assertThat(materialsWarehouse1).isNotEqualTo(materialsWarehouse2);
        materialsWarehouse1.setId(null);
        assertThat(materialsWarehouse1).isNotEqualTo(materialsWarehouse2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MaterialsWarehouseDTO.class);
        MaterialsWarehouseDTO materialsWarehouseDTO1 = new MaterialsWarehouseDTO();
        materialsWarehouseDTO1.setId(1L);
        MaterialsWarehouseDTO materialsWarehouseDTO2 = new MaterialsWarehouseDTO();
        assertThat(materialsWarehouseDTO1).isNotEqualTo(materialsWarehouseDTO2);
        materialsWarehouseDTO2.setId(materialsWarehouseDTO1.getId());
        assertThat(materialsWarehouseDTO1).isEqualTo(materialsWarehouseDTO2);
        materialsWarehouseDTO2.setId(2L);
        assertThat(materialsWarehouseDTO1).isNotEqualTo(materialsWarehouseDTO2);
        materialsWarehouseDTO1.setId(null);
        assertThat(materialsWarehouseDTO1).isNotEqualTo(materialsWarehouseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(materialsWarehouseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(materialsWarehouseMapper.fromId(null)).isNull();
    }
}
