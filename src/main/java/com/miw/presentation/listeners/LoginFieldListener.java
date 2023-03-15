package com.miw.presentation.listeners;

import javax.faces.event.AbortProcessingException;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;

import org.apache.logging.log4j.*;

public class LoginFieldListener implements ValueChangeListener {

	Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public void processValueChange(ValueChangeEvent event) throws AbortProcessingException {
		logger.debug("Value changed in login field "+event.getOldValue()+ "->" + event.getNewValue());
	}

}
