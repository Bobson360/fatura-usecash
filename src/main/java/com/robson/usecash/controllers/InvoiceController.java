package com.robson.usecash.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.robson.usecash.services.InvoiceService;

@RestController
@RequestMapping("/fatura")
public class InvoiceController {
	
	@Autowired
	InvoiceService invoiceService;
	
	@GetMapping("/gerar/{id}")
	public ResponseEntity<?> generateInvoice(@PathVariable Long id) {
		System.out.println(id);
		return invoiceService.generateInvoice(id);
	}
	
	@GetMapping("/gerar")
	public List<ResponseEntity<?>> generateInvoices() {
		return invoiceService.generateInvoice();
	}
	
	@GetMapping("/gerar/pdf/{id}")
	public ResponseEntity<?> generateInvoicePDF(@PathVariable Long id) throws IOException, DocumentException {
		return invoiceService.generateInvoicePdf(id);
	}
	
	@PutMapping("updade/{id}")
	public ResponseEntity<?> updateDueDate(@PathVariable Long id) {
		return invoiceService.updateInvoiceDueDate(id);
	}



}
