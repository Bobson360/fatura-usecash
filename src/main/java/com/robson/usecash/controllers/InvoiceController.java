package com.robson.usecash.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robson.usecash.services.InvoiceService;

@RestController
@RequestMapping("/fatura")
public class InvoiceController {
	
	@Autowired
	InvoiceService invoiceService;
	
	@GetMapping("/gerar/{id}")
	public ResponseEntity<?> generateInvoice(@PathVariable int id) {
		return invoiceService.getRegistry(id);
	}
	
	@PostMapping("/gerar")
	public ResponseEntity<String> generateInvoices(@RequestBody String ids) {
		System.out.println(ids);
		return null;
	}
	
	@PutMapping("updade/{id}")
	public void updateDueDate(@PathVariable int id) {
		System.out.println(id);
	}



}
