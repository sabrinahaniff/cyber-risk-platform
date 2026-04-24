package com.cyberrisk.cyber_risk_platform.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String industry;

    @Column(nullable = false)
    private Integer employeeCount;

    @Column(nullable = false)
    private String country;

    // Risk factors
    private Boolean hasFirewall;
    private Boolean hasAntiVirus;
    private Boolean encryptsData;
    private Boolean hasIncidentResponsePlan;
    private Boolean hasSecurityTraining;
    private Integer previousBreaches;

    // Calculated later
    private Integer riskScore;
}