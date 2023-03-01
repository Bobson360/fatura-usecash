package com.robson.usecash.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.robson.usecash.domain.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long>{

}
