package com.cyberrisk.cyber_risk_platform;

import com.cyberrisk.cyber_risk_platform.model.Company;
import com.cyberrisk.cyber_risk_platform.service.RiskScoringService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RiskScoringServiceTest {

    private RiskScoringService riskScoringService;

    @BeforeEach
    void setUp() {
        riskScoringService = new RiskScoringService();
    }

    @Test
    void perfectScore_whenAllControlsPresent() {
        Company company = new Company();
        company.setHasFirewall(true);
        company.setHasAntiVirus(true);
        company.setEncryptsData(true);
        company.setHasIncidentResponsePlan(true);
        company.setHasSecurityTraining(true);
        company.setPreviousBreaches(0);

        int score = riskScoringService.calculateRiskScore(company);

        assertEquals(100, score);
    }

    @Test
    void deducts20_whenNoFirewall() {
        Company company = new Company();
        company.setHasFirewall(false);
        company.setHasAntiVirus(true);
        company.setEncryptsData(true);
        company.setHasIncidentResponsePlan(true);
        company.setHasSecurityTraining(true);
        company.setPreviousBreaches(0);

        int score = riskScoringService.calculateRiskScore(company);

        assertEquals(80, score);
    }

    @Test
    void deducts15_whenNoAntivirus() {
        Company company = new Company();
        company.setHasFirewall(true);
        company.setHasAntiVirus(false);
        company.setEncryptsData(true);
        company.setHasIncidentResponsePlan(true);
        company.setHasSecurityTraining(true);
        company.setPreviousBreaches(0);

        int score = riskScoringService.calculateRiskScore(company);

        assertEquals(85, score);
    }

    @Test
    void deducts10PerBreach_whenPreviousBreaches() {
        Company company = new Company();
        company.setHasFirewall(true);
        company.setHasAntiVirus(true);
        company.setEncryptsData(true);
        company.setHasIncidentResponsePlan(true);
        company.setHasSecurityTraining(true);
        company.setPreviousBreaches(3);

        int score = riskScoringService.calculateRiskScore(company);

        assertEquals(70, score);
    }

    @Test
    void scoreNeverGoesBelowZero_whenManyDeductions() {
        Company company = new Company();
        company.setHasFirewall(false);
        company.setHasAntiVirus(false);
        company.setEncryptsData(false);
        company.setHasIncidentResponsePlan(false);
        company.setHasSecurityTraining(false);
        company.setPreviousBreaches(100);

        int score = riskScoringService.calculateRiskScore(company);

        assertEquals(0, score);
    }

    @Test
    void getRiskLevel_returnsCorrectLevel() {
        assertEquals("LOW", riskScoringService.getRiskLevel(85));
        assertEquals("MEDIUM", riskScoringService.getRiskLevel(65));
        assertEquals("HIGH", riskScoringService.getRiskLevel(45));
        assertEquals("CRITICAL", riskScoringService.getRiskLevel(20));
    }
}