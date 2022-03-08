package com.practice.microservice.limitsservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.practice.microservice.limitsservice.bean.Limits;
import com.practice.microservice.limitsservice.configuration.LimitsServiceConfiguration;

@RestController
public class LimitsController {
	@Autowired
	private LimitsServiceConfiguration configuration;

	@GetMapping("/limits")
	public Limits retrieveLimits() {
		// return new Limits(1,1000);
		return new Limits(configuration.getMinimum(), configuration.getMaximum());
	}
}
