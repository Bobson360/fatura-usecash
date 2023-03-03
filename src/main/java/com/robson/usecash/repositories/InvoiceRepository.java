package com.robson.usecash.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.robson.usecash.domain.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
	
	  
	  List<Invoice> findByValorTotalFaturaAndDataVencimentoFaturaAndRegistry_CNPJ(
				Double valorTotalFinalFatura, LocalDate dataVectoFaturaCobranca, String cnpj
			  );
}
