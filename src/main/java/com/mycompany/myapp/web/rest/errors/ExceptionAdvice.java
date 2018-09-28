package com.mycompany.myapp.web.rest.errors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



/**
 * All exceptions ever thrown in time of processing request will be intercepted 
 * and routed to this class handlers.
 * */
@RestControllerAdvice("com.mycompany.myapp.web.rest")
@Component
public class ExceptionAdvice {
	
	@ExceptionHandler(Error.class)
	public ResponseEntity<Error> errorHandler(Error e) {
		return ResponseEntity.status(e.getStatus()).body(e);
	}

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> errorHandler(ConstraintViolationException e) {
        Error error = Error.builder()
            .withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
            .withDetail("You sended invalid data change it and try again.")
            .withCode("dataerror")
            .withTitle("Invalid data.")
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
	
	@ExceptionHandler(NotEnoughIngredientsException.class)
	public ResponseEntity<Error> coffeeAssemblyErrorHandler(
			NotEnoughIngredientsException e) {
		
		StringBuilder details = new StringBuilder("To assembly ");
		details.append(e.getEntityName().orElse("requested entity"));
		details.append(" you need to add the following inredients:");
		
		e.getTitleAmountNeededPair().forEach((String title, Integer amount) -> {
			details.append('\n');
			details.append(title);
			details.append(": ");
			details.append(amount);
		});
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Error.builder()
					.withCode("noingr")
					.withDetail(details.toString())
					.withStatus(HttpStatus.INTERNAL_SERVER_ERROR)
					.withTitle("There is not enough ingredients to assembly entity.")
					.build()
				);
	}
}
