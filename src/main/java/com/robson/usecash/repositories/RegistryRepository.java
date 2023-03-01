package com.robson.usecash.repositories;

import com.robson.usecash.domain.Registry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RegistryRepository extends JpaRepository<Registry, Long> {
}
