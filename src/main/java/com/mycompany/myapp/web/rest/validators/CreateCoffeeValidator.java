package com.mycompany.myapp.web.rest.validators;

import com.mycompany.myapp.service.dto.CoffeeDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CreateCoffeeValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return CoffeeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CoffeeDTO coffee = (CoffeeDTO) target;

        if (coffee.getId() != null) {
            errors.rejectValue("id", "idexists","A new coffee cannot already have an ID");
        }

        if (coffee.getTitle() == null) {
            errors.rejectValue("title", "paramnull","A new coffee must have title");
        }
    }
}
