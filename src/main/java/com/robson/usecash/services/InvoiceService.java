package com.robson.usecash.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.robson.usecash.domain.Invoice;
import com.robson.usecash.domain.Registry;
import com.robson.usecash.repositories.InvoiceRepository;
import com.robson.usecash.repositories.RegistryRepository;

@Service
public class InvoiceService {

	@Autowired
	InvoiceRepository invoiceRepository;
	
	@Autowired
	RegistryRepository registryRepository;
	
	public void getInvoice(int id) {
		invoiceRepository.findById((long) id);
	}
	
	public ResponseEntity<?> getRegistry(int id) {
		 Optional<Registry> registry =  registryRepository.findById((long) id);

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
		
		Optional<Registry> registry_found = registryRepository.findById((long) id);
		Registry registry = registry_found.get();
		
		Double REGIME_ESPECIAL = registry.getTAXA_REGIME_ESPECIAL() * registry.getTOTAL_CREDITO_ADQUIRIDO();
		Double CORREIOS = registry.getCORREIOS_1()+registry.getCORREIOS_2()+registry.getCORREIOS_3()+registry.getCORREIOS_4();
		Double TERMO = (registry.getVALOR_UNITARIO_TERMO_ADESAO() * registry.getQTDE_TERMOS_CANCELADOS()) + registry.getQTDE_TERMOS_EMITIDOS();
		Double EMISSAO_CARTAO = registry.getVALOR_UNITARIO_EMISSAO_CARTAO() * registry.getQTDE_CARTAO_EMITIDOS();
		Double MENSALIDADE = registry.getQTDE_LOJA() * registry.getVALOR_MENSALIDADE();
		
		Double FATURA = REGIME_ESPECIAL+CORREIOS+TERMO+EMISSAO_CARTAO+MENSALIDADE;
		LocalDate DUE_DATE = calcDueDate(registry.getDIAS_UTEIS_VECTO_BOLETO());
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
		if(FATURA > 25000) {
			registry.setSTATUS("BLOQUEADO");
			
	        Map<String, Object> error = new LinkedHashMap<>();
	        error.put("registry_id", id);
	        error.put("msg", "billing amount exceeded the allowable limit of BRL 25,000.00. Registration has been blocked. Consult the administrator.");
	        error.put("status", HttpStatus.BAD_REQUEST);
	        error.put("code", HttpStatus.BAD_REQUEST.value());
	        error.put("registry", registryRepository.save(registry));
			System.out.println("Fatura superior há R$ 25.000,00 registro bloqueado");
			return ResponseEntity.badRequest().body(error);
		}
		
		Invoice invoice = new Invoice();
		invoice.setVALOR_TOTAL_FINAL_FATURA(FATURA);
		invoice.setTOTAL_MENSALIDADE(MENSALIDADE);
		invoice.setTOTAL_PAGAR_EMISSAO_CARTAO(EMISSAO_CARTAO);
		invoice.setTOTAL_PAGAR_TERMO(TERMO);
		invoice.setTOTAL_PAGAR_CORREIOS(CORREIOS);
		invoice.setTOTAL_PAGAR_REGIME_ESPECIAL(REGIME_ESPECIAL);
		invoice.setDATA_VECTO_FATURA_COBRANCA(DUE_DATE);
		
		 if (registry_found.isPresent()) {
				return ResponseEntity.ok().body(invoiceRepository.save(invoice));
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

		        return date;
		    }

}
