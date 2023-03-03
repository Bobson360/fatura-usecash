package com.robson.usecash.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {
	@GetMapping("/")
	public ResponseEntity<?> index() {
		Map<String, Object> msg = new LinkedHashMap<>();
		msg.put("Author", "Robson Rodrigues da Silva");
		msg.put("Version", "0.1.1");
		msg.put("Since", "02/2023");
		msg.put("Description", "Gerador de fatura com spring boot");
		return ResponseEntity.ok(msg);
	}
}
