package com.example.swaggerserver.controller;

import com.example.swaggerserver.config.ServiceDefinitionsContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceDefinitionController {

    @Autowired
    private ServiceDefinitionsContext definitionContext;

    @GetMapping("/service/{servicename}")
    public String getServiceDefinition(@PathVariable("servicename") String serviceName) {

        return definitionContext.getSwaggerDefinition(serviceName);

    }
}