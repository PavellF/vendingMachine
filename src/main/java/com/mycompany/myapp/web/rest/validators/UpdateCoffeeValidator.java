package com.mycompany.myapp.web.rest.validators;

import com.mycompany.myapp.service.CoffeeService;
import com.mycompany.myapp.service.dto.CoffeeDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UpdateCoffeeValidator implements Validator {

    private final CoffeeService coffeeService;

    public UpdateCoffeeValidator(CoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CoffeeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CoffeeDTO coffee = (CoffeeDTO) target;
        Long id = coffee.getId();

        if (id == null) {
            errors.rejectValue("id", "idnull", "Id must not be null");
            return;
        }

        CoffeeDTO existing = coffeeService.findOne(id).orElse(null);

        if (existing == null) {
            errors.reject("noobject", null,"Could not find object with id " + id);
            return;
        }

        if (coffee.getTitle() == null) {
            coffee.setTitle(existing.getTitle());
        }
    }
}
