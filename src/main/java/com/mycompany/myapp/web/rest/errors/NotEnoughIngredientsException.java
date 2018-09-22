package com.mycompany.myapp.web.rest.errors;

import java.util.Map;
import java.util.Optional;

import java.util.Collections;

/**
 * If there is not enough ingredients to assembly some entity.
 * */
public class NotEnoughIngredientsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private Map<String, Integer> titleAmountNeededPair;
	private Optional<String> entityName;

	public NotEnoughIngredientsException(String entityName, 
			Map<String, Integer> titleAmountNeededPair) {
		
		if (titleAmountNeededPair == null || titleAmountNeededPair.isEmpty()) {
			throw new NullPointerException();
		}
		
		this.titleAmountNeededPair = titleAmountNeededPair;
		this.entityName = Optional.ofNullable(entityName);
		
	}
	
	public NotEnoughIngredientsException(Map<String, Integer> titleAmountNeededPair) {
		this(null, titleAmountNeededPair);
	}
	
	public Optional<String> getEntityName() {
		return entityName;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = Optional.ofNullable(entityName);
	}

	public NotEnoughIngredientsException addTitleAmountNeeded(
			String entityTitle, Integer amount) {
		titleAmountNeededPair.put(entityTitle, amount);
		return this;
	}

	/**
	 * Never {@code null} or empty.
	 * */
	public Map<String, Integer> getTitleAmountNeededPair() {
		return Collections.unmodifiableMap(titleAmountNeededPair);
	}

}
