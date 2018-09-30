package com.mycompany.myapp.web.rest.validators;

import com.mycompany.myapp.service.MaterialsWarehouseService;
import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;
import com.mycompany.myapp.web.rest.errors.Error;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.net.URI;
import java.util.Optional;

public class UpdateMaterialsWarehouseValidator implements Validator {

    private MaterialsWarehouseService materialsWarehouseService;

    public UpdateMaterialsWarehouseValidator(MaterialsWarehouseService materialsWarehouseService) {
        this.materialsWarehouseService = materialsWarehouseService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MaterialsWarehouseDTO.class.equals(clazz);
    }

    private void mergeNull(MaterialsWarehouseDTO existing, MaterialsWarehouseDTO material) {
        if (material.getLeft() == null) {
            material.setLeft(existing.getLeft());
        }

        if (material.getMaxAmount() == null) {
            material.setMaxAmount(existing.getMaxAmount());
        }

        if (material.getTitle() == null) {
            material.setTitle(existing.getTitle());
        }
    }

    @Override
    public void validate(Object target, Errors errors) {
        MaterialsWarehouseDTO material = (MaterialsWarehouseDTO) target;
        Long id = material.getId();

        if (id == null) {
            errors.rejectValue("id", "idnull", "Id must not be null");
            return;
        }

        MaterialsWarehouseDTO existing = materialsWarehouseService.findOne(id).orElse(null);

        if (existing == null) {
            errors.reject("noobject", null,"Could not find object with id " + id);
            return;
        }

        mergeNull(existing,material);
        Integer materialsAmount = material.getLeft();
        Integer maximumAmountAllowed = material.getMaxAmount();

        if (materialsAmount > maximumAmountAllowed) {
            errors.rejectValue("left", "overflow","Amount left cannot be greater " +
                "than max amount of material.");
        }
    }
}
