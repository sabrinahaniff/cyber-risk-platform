package com.cyberrisk.cyber_risk_platform.controller;

import com.cyberrisk.cyber_risk_platform.model.Company;
import com.cyberrisk.cyber_risk_platform.service.CompanyService;
import com.cyberrisk.cyber_risk_platform.service.MemoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final MemoService memoService;

    public CompanyController(CompanyService companyService, MemoService memoService) {
        this.companyService = companyService;
        this.memoService = memoService;
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company saved = companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/generate-memo")
    public ResponseEntity<String> generateMemo(@PathVariable Long id) {
        try {
            Company company = companyService.getCompanyById(id);
            String memo = memoService.generateMemo(company);
            return ResponseEntity.ok(memo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error generating memo: " + e.getMessage());
        }
    }
}