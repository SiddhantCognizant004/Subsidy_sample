package com.cts.agrichain.subsidy.service;

import com.cts.agrichain.subsidy.dao.DisbursementRepo;
import com.cts.agrichain.subsidy.dao.SubsidyProgramRepo;
import com.cts.agrichain.subsidy.dto.DisbursementDTO;
import com.cts.agrichain.subsidy.dto.FarmerDTO;
import com.cts.agrichain.subsidy.entity.Disbursement;
import com.cts.agrichain.subsidy.entity.SubsidyProgram;
import com.cts.agrichain.subsidy.enums.DisbursementStatus;
import com.cts.agrichain.subsidy.enums.SubsidyStatus;
import com.cts.agrichain.subsidy.feign.FarmerClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DisbursementService {

    @Autowired
    private DisbursementRepo disbursementRepo;

    @Autowired
    private SubsidyProgramRepo subsidyProgramRepo;

    @Autowired
    private FarmerClient farmerClient;

    @CircuitBreaker(name = "farmerService", fallbackMethod = "applyForSubsidyFallback")
    public Disbursement applyForSubsidy(DisbursementDTO dto) {

        FarmerDTO farmer = farmerClient.getFarmerById(dto.getFarmerId());

        if (farmer == null || farmer.getFarmerId() == -1) {
            throw new RuntimeException("Farmer Service is currently unavailable. Please try again later.");
        }

        SubsidyProgram program = subsidyProgramRepo.findById(dto.getProgramId())
                .orElseThrow(() -> new RuntimeException("Subsidy Program not found with ID: " + dto.getProgramId()));

        // Reject if subsidy budget is already exhausted
        if (program.getSubsidyStatus() == SubsidyStatus.VALIDATED) {
            throw new RuntimeException("Subsidy Program budget is fully consumed. No more disbursements accepted.");
        }

        // Reject if farmer has already applied for this subsidy program
        boolean alreadyApplied = disbursementRepo.existsByFarmerIdAndSubsidyProgram_ProgramID(
                dto.getFarmerId(), dto.getProgramId()
        );
        if (alreadyApplied) {
            throw new RuntimeException(
                    "Farmer with ID " + dto.getFarmerId() +
                            " has already applied for subsidy program ID " + dto.getProgramId() +
                            ". A farmer can only apply once per subsidy program."
            );
        }

        Disbursement disbursement = new Disbursement();
        disbursement.setFarmerId(dto.getFarmerId());
        disbursement.setSubsidyProgram(program);
        disbursement.setDisbursementAmount(dto.getDisbursementAmount());
        disbursement.setDisbursementDate(LocalDate.now());
        disbursement.setDisbursementStatus(DisbursementStatus.PENDING);

        return disbursementRepo.save(disbursement);
    }

    public Disbursement applyForSubsidyFallback(DisbursementDTO dto, Throwable ex) {
        throw new RuntimeException(
                "Farmer Service is down. Cannot process disbursement for farmer ID: "
                        + dto.getFarmerId() + ". Reason: " + ex.getMessage()
        );
    }

    public Disbursement getById(int id) {
        return disbursementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Disbursement not found with ID: " + id));
    }

    public List<Disbursement> getAll() {
        return disbursementRepo.findAll();
    }

    public Disbursement reviewDisbursement(int id, DisbursementStatus status) {
        Disbursement disbursement = disbursementRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Disbursement not found with ID: " + id));

        disbursement.setDisbursementStatus(status);
        disbursementRepo.save(disbursement);

        // When a disbursement is approved (COMPLETED), update consumedBudget
        if (status == DisbursementStatus.COMPLETED) {
            SubsidyProgram program = disbursement.getSubsidyProgram();

            // Null-safe: legacy records created before the fix may have consumedBudget = null
            double currentConsumed = program.getConsumedBudget() != null ? program.getConsumedBudget() : 0.0;
            double newConsumed = currentConsumed + disbursement.getDisbursementAmount();

            // BLOCK approval if it would exceed the allotted budget
            if (newConsumed > program.getAllottedBudget()) {
                // Roll back the status to REJECTED since we cannot approve
                disbursement.setDisbursementStatus(DisbursementStatus.REJECTED);
                disbursementRepo.save(disbursement);
                throw new RuntimeException(
                        "Budget exceeded! Approving ₹" + disbursement.getDisbursementAmount() +
                                " would exceed allotted budget of ₹" + program.getAllottedBudget() +
                                ". Remaining budget: ₹" + (program.getAllottedBudget() - currentConsumed) +
                                ". Disbursement has been auto-rejected."
                );
            }

            program.setConsumedBudget(newConsumed);

            // PENDING   → budget still has room, subsidy is active
            // VALIDATED → budget fully consumed, subsidy is closed
            if (newConsumed >= program.getAllottedBudget()) {
                program.setSubsidyStatus(SubsidyStatus.VALIDATED);
            } else {
                program.setSubsidyStatus(SubsidyStatus.PENDING);
            }

            subsidyProgramRepo.save(program);
        }

        return disbursement;
    }

    public List<Disbursement> getByFarmerId(Long farmerId) {
        return disbursementRepo.findByFarmerId(farmerId);
    }

    public List<Disbursement> getByProgramId(int programId) {
        return disbursementRepo.findBySubsidyProgram_ProgramID(programId);
    }
}