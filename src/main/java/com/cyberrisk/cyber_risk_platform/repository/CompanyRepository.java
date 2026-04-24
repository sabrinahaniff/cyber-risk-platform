package com.cyberrisk.cyber_risk_platform.repository;

import com.cyberrisk.cyber_risk_platform.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

}