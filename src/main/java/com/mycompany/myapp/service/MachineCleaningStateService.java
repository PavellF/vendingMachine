package com.mycompany.myapp.service;

/**
 * Service Interface for managing current machine state. 
 * */
public interface MachineCleaningStateService {

	/**
	 * Makes machine closer to cleaning state.
	 * @return false means cleaning needed, could not increase counter.
	 * */
	boolean increaseCounter();
	
	/**
	 * Cleans machine.
	 * */
	void clean();
	
}
