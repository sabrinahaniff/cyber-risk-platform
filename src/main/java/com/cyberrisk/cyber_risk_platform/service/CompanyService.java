package com.cyberrisk.cyber_risk_platform.service;

import com.cyberrisk.cyber_risk_platform.model.Company;
import com.cyberrisk.cyber_risk_platform.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final RiskScoringService riskScoringService;

    public CompanyService(CompanyRepository companyRepository, RiskScoringService riskScoringService) {
        this.companyRepository = companyRepository;
        this.riskScoringService = riskScoringService;
    }

    public Company createCompany(Company company) {
        log.info("Creating risk assessment for company: {}", company.getName());
        int score = riskScoringService.calculateRiskScore(company);
        company.setRiskScore(score);
        Company saved = companyRepository.save(company);
        log.info("Risk assessment complete for: {} | Score: {} | Level: {}",
            company.getName(), score, riskScoringService.getRiskLevel(score));
        return saved;
    }

    public List<Company> getAllCompanies() {
        log.info("Fetching all companies");
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        log.info("Fetching company with id: {}", id);
        return companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Company not found with id: {}", id);
                    return new RuntimeException("Company not found with id: " + id);
                });
    }

    public void deleteCompany(Long id) {
        log.info("Deleting company with id: {}", id);
        companyRepository.deleteById(id);
        log.info("Company deleted successfully: {}", id);
    }
}