package com.robson.usecash.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
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
	
	public ResponseEntity<?> calculateInvoice(int id) {
		
		 Map<String, Object> response = new HashMap<>();
		Optional<Registry> registry = csvDataRepository.findById((long) id);
		
		Double REGIME_ESPECIAL = registry.get().getTAXA_REGIME_ESPECIAL() * registry.get().getTOTAL_CREDITO_ADQUIRIDO();
		Double CORREIOS = registry.get().getCORREIOS_1()+registry.get().getCORREIOS_2()+registry.get().getCORREIOS_3()+registry.get().getCORREIOS_4();
		Double TERMO = (registry.get().getVALOR_UNITARIO_TERMO_ADESAO() * registry.get().getQTDE_TERMOS_CANCELADOS()) + registry.get().getQTDE_TERMOS_EMITIDOS();
		Double EMISSAO_CARTAO = registry.get().getVALOR_UNITARIO_EMISSAO_CARTAO() * registry.get().getQTDE_CARTAO_EMITIDOS();
		Double MENSALIDADE = registry.get().getQTDE_LOJA() * registry.get().getVALOR_MENSALIDADE();
		
		Double FATURA = REGIME_ESPECIAL+CORREIOS+TERMO+EMISSAO_CARTAO+MENSALIDADE;
		LocalDate DUE_DATE = calcDueDate(registry.get().getDIAS_UTEIS_VECTO_BOLETO());
		/*
		 * ok: obter dados
		 *
		 * calcular valor total
		 * 		VALOR TOTAL FINAL FATURA:
		 * 			OK:	TOTAL A PAGAR REGIME ESPECIAL R$: TAXA DE REGIME ESPECIAL	 *	TOTAL DE CRÉDITO ADQUIRIDO
		 * 			+
		 * 			TOTAL A PAGAR CORREIOS R$: CORREIOS 1 - VALOR TOTAL R$	 +	CORREIOS 2 - VALOR TOTAL R$	 +	CORREIOS 3 - VALOR TOTAL R$  +	CORREIOS 4 - VALOR TOTAL R$
		 * 			+
		 * 			TOTAL A PAGAR TERMO: (VALOR UNITARIO TERMO DE ADESAO   * 	QTDE DE TERMOS CANCELADOS)   +	QTDE DE TERMOS EMITIDOS
		 * 			+
		 * 			TOTAL A PAGAR EMISSAO DE CARTAO: VALOR  UNITARIO EMISSAO CARTÃO  *	QTDE CARTAO EMITIDOS 
		 * 			+
		 * 			TOTAL DA MENSALIDADE R$: QTDE DE LOJA  *	VALOR MENSALIDADE
		 * 			
		 * Calcular data de vencimento
		 * 			 
		 * checar duplicidade
		 * 
		 * emitir fatura
		 */
		
		response.put("Valor total da fatura", FATURA);
		response.put("Mensalidade Plataforma ", MENSALIDADE);
		response.put("Emissão de cartão", EMISSAO_CARTAO);
		response.put("emissão termo de adesão", TERMO);
		response.put("Taxa de regime especial", REGIME_ESPECIAL);
		
		
		
		 if (registry.isPresent()) {
				return ResponseEntity.ok().body(response);
		    } else {
		        Map<String, Object> error = new LinkedHashMap<>();
		        error.put("registry_id", id);
		        error.put("msg", "error, registry not found!");
		        error.put("status", HttpStatus.NOT_FOUND);
		        error.put("code", HttpStatus.NOT_FOUND.value());
		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		    }
	}
	
	 public LocalDate calcDueDate(int BUSINESS_DAYS_UNTIL_INVOICE_DUE_DATE) {
		        LocalDate startDate = LocalDate.now(); // Start date

		        LocalDate date = startDate;
		        int businessDays = 0;

		        while (businessDays < BUSINESS_DAYS_UNTIL_INVOICE_DUE_DATE) {
		            date = date.plus(1, ChronoUnit.DAYS);
		            if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
		                businessDays++;
		            }
		        }

		        System.out.println("End date: " + date);
		        return date;
		    }
	 

}
