package com.mycompany.myapp.web.rest.validators;

import com.mycompany.myapp.service.dto.CofeeMaterialDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

public class CreateCoffeeMaterialValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return CofeeMaterialDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CofeeMaterialDTO material = (CofeeMaterialDTO) target;

        if(material.getId() != null) {
            errors.rejectValue("id", "idexists","A new cofeeMaterial already have an ID");
        }

        List<String> rejected = new ArrayList<>(3);

        if (material.getAmount() == null) {
            rejected.add("amount");
        }

        if (material.getCoffeeId() == null) {
            rejected.add("coffeeId");
        }

        if (material.getMaterialsWarehouseId() == null) {
            rejected.add("materialsWarehouseId");
        }

        if (!rejected.isEmpty()) {
            errors.reject("paramnull", null,
                "The following parameters must not be null: " + String.join(" ", rejected));
        }
    }
}
