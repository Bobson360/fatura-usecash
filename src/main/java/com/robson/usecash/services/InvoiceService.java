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
		
		if(checkDuplicateInvoice(FATURA, DUE_DATE, registry.getCNPJ()))
			return errorReturn(HttpStatus.BAD_REQUEST, "There is already an invoice with the same value month and cnpj.");
		
		if(FATURA > 25000) {
			registry.setSTATUS("BLOQUEADO");
			return errorReturn(HttpStatus.BAD_REQUEST, "billing amount exceeded the allowable limit of BRL 25,000.00. Registration has been blocked. Consult the administrator.");
		} else
			registry.setSTATUS("PROCESSADO");
		
		
		Invoice invoice = new Invoice();
		invoice.setRegistry(registry);
		invoice.setValorTotalFatura(FATURA);
		invoice.setTotalMensalidade(MENSALIDADE);
		invoice.setTotalPagarEmissaoCartao(EMISSAO_CARTAO);
		invoice.setTotalPAgarTermo(TERMO);
		invoice.setTotalPagarCorreios(CORREIOS);
		invoice.setTotalPagarRegimeEspecial(REGIME_ESPECIAL);
		invoice.setDataVencimentoFatura(DUE_DATE);
		
		 if (registry_found.isPresent()) {
				return ResponseEntity.ok().body(invoiceRepository.save(invoice));
		    } else {
		        return errorReturn(HttpStatus.NOT_FOUND, "error, registry not found!");
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
	 
	 public boolean checkDuplicateInvoice(Double valorTotalFinalFatura, LocalDate dataVectoFaturaCobranca, String cnpj) {
		 return invoiceRepository.findByValorTotalFaturaAndDataVencimentoFaturaAndRegistry_CNPJ(valorTotalFinalFatura, dataVectoFaturaCobranca, cnpj).size() > 0;
	 }
	 
	 public ResponseEntity<?> errorReturn(HttpStatus statusCode, String message) {
		   Map<String, Object> error = new LinkedHashMap<>();
	        error.put("msg", message);
	        error.put("status", statusCode);
	        error.put("code", statusCode.value());
	        return ResponseEntity.status(statusCode).body(error);
	 }
}
