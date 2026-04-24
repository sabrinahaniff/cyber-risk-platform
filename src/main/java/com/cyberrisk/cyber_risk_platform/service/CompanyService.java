package com.cyberrisk.cyber_risk_platform.service;

import com.cyberrisk.cyber_risk_platform.model.Company;
import com.cyberrisk.cyber_risk_platform.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final RiskScoringService riskScoringService;

    public CompanyService(CompanyRepository companyRepository, RiskScoringService riskScoringService) {
        this.companyRepository = companyRepository;
        this.riskScoringService = riskScoringService;
    }

    public Company createCompany(Company company) {
        int score = riskScoringService.calculateRiskScore(company);
        company.setRiskScore(score);
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + id));
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}