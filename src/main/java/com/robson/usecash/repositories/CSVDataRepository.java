package com.robson.usecash.repositories;

import com.robson.usecash.domain.CSVData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CSVDataRepository extends JpaRepository<CSVData, Long> {
}
