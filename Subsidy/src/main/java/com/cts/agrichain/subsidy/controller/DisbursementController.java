package com.cts.agrichain.subsidy.controller;

import com.cts.agrichain.subsidy.dto.DisbursementDTO;
import com.cts.agrichain.subsidy.entity.Disbursement;
import com.cts.agrichain.subsidy.enums.DisbursementStatus;
import com.cts.agrichain.subsidy.service.DisbursementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disbursements")
public class DisbursementController {

    @Autowired
    private DisbursementService disbursementService;

    // CREATE using DTO
    @PostMapping
    public Disbursement applyForSubsidy(@RequestBody DisbursementDTO dto) {
        return disbursementService.applyForSubsidy(dto);
    }

    @GetMapping("/{id}")
    public Disbursement getById(@PathVariable int id) {
        return disbursementService.getById(id);
    }

    @GetMapping
    public List<Disbursement> getAll() {
        return disbursementService.getAll();
    }

    @PatchMapping("/{id}/review")
    public Disbursement reviewDisbursement(@PathVariable int id,
                                           @RequestParam DisbursementStatus status) {
        return disbursementService.reviewDisbursement(id, status);
    }

    @GetMapping("/farmer/{farmerId}")
    public List<Disbursement> getByFarmer(@PathVariable Long farmerId) {
        return disbursementService.getByFarmerId(farmerId);
    }

    @GetMapping("/program/{programId}")
    public List<Disbursement> getByProgram(@PathVariable int programId) {
        return disbursementService.getByProgramId(programId);
    }
}