package com.mycompany.myapp.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mycompany.myapp.service.MachineCleaningStateService;

/**
 * Basic implementation for {@code MachineCleaningStateService}.
 **/
@Service
public class MachineCleaningStateServiceImpl
		implements MachineCleaningStateService {

	private volatile int counter = 0;
	
	@Value("${counterMaxValue}")
	private Integer maxAmount;
	
	public MachineCleaningStateServiceImpl() {
		
		synchronized(this) {
			if (maxAmount == null) {
				maxAmount = 8;
			}
		}
		
	}

	@Override
	public synchronized boolean increaseCounter() {
		if (counter < maxAmount) {
			counter = counter + 1;
			return true;
		}
		return false;
	}

	@Override
	public synchronized boolean clean() {
		if (counter == maxAmount) {
			counter = 0;
			return true;
		}
		return false;
	}

}
