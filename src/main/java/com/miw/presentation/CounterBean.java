package com.miw.presentation;

import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

import org.apache.logging.log4j.*;

@Named ("counter")
@ApplicationScoped
public class CounterBean {
	
	Logger logger = LogManager.getLogger(this.getClass());
	
	public CounterBean() {
		logger.debug("Initializing counter");
	}
	Integer count = 0 ;
	public Integer inc()
	{
		return ++ count;
	}
	public Integer getCount() {
		
		return count;
	}
}
