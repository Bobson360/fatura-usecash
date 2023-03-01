package com.robson.usecash.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.robson.usecash.domain.Registry;
import com.robson.usecash.repositories.InvoiceRepository;
import com.robson.usecash.repositories.RegistryRepository;

@Service
public class InvoiceService {

	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	RegistryRepository csvDataRepository;
	
	public void getInvoice(int id) {
		invoiceRepository.findById((long) id);
	}
	
	public ResponseEntity<?> getRegistry(int id) {
		 Optional<Registry> registry =  csvDataRepository.findById((long) id);

		    if (registry.isPresent()) {
		        return ResponseEntity.ok(registry.get());
		    } else {
		        Map<String, Object> error = new LinkedHashMap<>();
		        error.put("registry_id", id);
		        error.put("msg", "error, registry not found!");
		        error.put("status", HttpStatus.NOT_FOUND);
		        error.put("code", HttpStatus.NOT_FOUND.value());
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		    }
	}
	
	public void calculateInvoice() {
		/*
		 * obter dados
		 * calcular valor total
		 * checar duplicidade
		 * 
		 * emitir fatura
		 */
	}
}
