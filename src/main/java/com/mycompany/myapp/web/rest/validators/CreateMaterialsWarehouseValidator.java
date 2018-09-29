package com.mycompany.myapp.web.rest.validators;

import com.mycompany.myapp.service.dto.MaterialsWarehouseDTO;
import com.mycompany.myapp.web.rest.errors.Error;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CreateMaterialsWarehouseValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MaterialsWarehouseDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MaterialsWarehouseDTO material = (MaterialsWarehouseDTO) target;

        if (material.getId() != null) {
            errors.rejectValue("id", "idexists","A new materialsWarehouse cannot already " +
                "have an ID");
        }

        List<String> rejected = new ArrayList<>(3);
        Integer materialsAmount = material.getLeft();
        Integer maximumAmountAllowed = material.getMaxAmount();

        if (materialsAmount == null ) {
            rejected.add("left");
        }

        if (maximumAmountAllowed == null) {
            rejected.add("maxAmount");
        }

        if (material.getTitle() == null) {
            rejected.add("title");
        }

        if (!rejected.isEmpty()) {
            errors.reject("paramnull", null,
                "The following parameters must not be null: " + String.join(" ", rejected));
            return;
        }

        if (materialsAmount > maximumAmountAllowed) {
            errors.reject("overflow", null,"Amount left cannot be greater than " +
                "max amount of material.");
        }
    }
}
