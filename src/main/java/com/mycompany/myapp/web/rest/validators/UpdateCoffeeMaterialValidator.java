package com.mycompany.myapp.web.rest.validators;

import com.mycompany.myapp.service.CofeeMaterialService;
import com.mycompany.myapp.service.dto.CofeeMaterialDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UpdateCoffeeMaterialValidator implements Validator {

    private final CofeeMaterialService cofeeMaterialService;

    public UpdateCoffeeMaterialValidator(CofeeMaterialService cofeeMaterialService) {
        this.cofeeMaterialService = cofeeMaterialService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CofeeMaterialDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CofeeMaterialDTO material = (CofeeMaterialDTO) target;
        Long id = material.getId();

        if (id == null) {
            errors.rejectValue("id", "idnull", "Id must not be null");
            return;
        }

        CofeeMaterialDTO existing = cofeeMaterialService.findOne(id).orElse(null);

        if (existing == null) {
            errors.reject("noobject", null,"Could not find object with id " + id);
            return;
        }

        material.mergeNull(existing);

    }
}
