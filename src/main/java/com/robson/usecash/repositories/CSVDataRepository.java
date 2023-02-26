package com.robson.usecash.repositories;

import com.robson.usecash.domain.CSVData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSVDataRepository extends JpaRepository<CSVData, Long> {
}
