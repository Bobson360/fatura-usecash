package com.robson.usecash.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.robson.usecash.domain.Registry;

public interface RegistryRepository extends JpaRepository<Registry, Long> {
	@Query("SELECT r.id FROM Registry r WHERE r.id NOT IN (SELECT i.registry.id FROM Invoice i)")
    List<Long> findRegistryIdsWithoutInvoice();
}
