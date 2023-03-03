package com.robson.usecash.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robson.usecash.domain.Registry;
import com.robson.usecash.services.RegistryService;

@RestController
@RequestMapping("/registry")
@CrossOrigin(origins = "*")
public class RegistryController {

    @Autowired
    RegistryService registryService;

   
    @GetMapping("/registries")
    public  ResponseEntity<List<Registry>> getRegistries() {
    	System.out.println();
    	System.out.println("----------------------------------");
    	System.out.println("Lista de registros");
    	System.out.println("----------------------------------");
    	System.out.println();
    	return ResponseEntity.ok().body(registryService.getRegistries());
    }
    
  

}
