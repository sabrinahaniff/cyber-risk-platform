package com.cyberrisk.cyber_risk_platform.service;

import com.cyberrisk.cyber_risk_platform.model.Company;
import org.springframework.stereotype.Service;

@Service
public class RiskScoringService {

    public int calculateRiskScore(Company company) {
        int score = 100;

        if (Boolean.FALSE.equals(company.getHasFirewall())) score -= 20;
        if (Boolean.FALSE.equals(company.getHasAntiVirus())) score -= 15;
        if (Boolean.FALSE.equals(company.getEncryptsData())) score -= 20;
        if (Boolean.FALSE.equals(company.getHasIncidentResponsePlan())) score -= 15;
        if (Boolean.FALSE.equals(company.getHasSecurityTraining())) score -= 10;

        if (company.getPreviousBreaches() != null) {
            score -= company.getPreviousBreaches() * 10;
        }

        return Math.max(0, score); // never go below 0
    }

    public String getRiskLevel(int score) {
        if (score >= 80) return "LOW";
        if (score >= 60) return "MEDIUM";
        if (score >= 40) return "HIGH";
        return "CRITICAL";
    }
}