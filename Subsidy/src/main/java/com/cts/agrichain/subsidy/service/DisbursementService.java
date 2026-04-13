package com.cts.agrichain.subsidy.service;

import com.cts.agrichain.subsidy.dao.DisbursementRepo;
import com.cts.agrichain.subsidy.dao.SubsidyProgramRepo;
import com.cts.agrichain.subsidy.dto.DisbursementDTO;
import com.cts.agrichain.subsidy.dto.FarmerDTO;
import com.cts.agrichain.subsidy.entity.Disbursement;
import com.cts.agrichain.subsidy.entity.SubsidyProgram;
import com.cts.agrichain.subsidy.enums.DisbursementStatus;
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

    // ✅ @CircuitBreaker wraps the entire method
    //    name = "farmerService" must match the name in application.properties
    //    fallbackMethod = "applyForSubsidyFallback" runs if circuit is OPEN
    @CircuitBreaker(name = "farmerService", fallbackMethod = "applyForSubsidyFallback")
    public Disbursement applyForSubsidy(DisbursementDTO dto) {

        // Feign calls FARMER-SERVICE — load balancer picks the right instance
        FarmerDTO farmer = farmerClient.getFarmerById(dto.getFarmerId());

        // Check if we got the fallback dummy response
        if (farmer == null || farmer.getFarmerId() == -1) {
            throw new RuntimeException("Farmer Service is currently unavailable. Please try again later.");
        }

        SubsidyProgram program = subsidyProgramRepo.findById(dto.getProgramId())
                .orElseThrow(() -> new RuntimeException("Subsidy Program not found with ID: " + dto.getProgramId()));

        Disbursement disbursement = new Disbursement();
        disbursement.setFarmerId(dto.getFarmerId());
        disbursement.setSubsidyProgram(program);
        disbursement.setDisbursementAmount(dto.getDisbursementAmount());
        disbursement.setDisbursementDate(LocalDate.now());
        disbursement.setDisbursementStatus(DisbursementStatus.PENDING);

        return disbursementRepo.save(disbursement);
    }

    // ✅ Fallback method — called when circuit is OPEN (farmer service keeps failing)
    //    Signature must match the original method + an extra Throwable parameter
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
        return disbursementRepo.save(disbursement);
    }

    public List<Disbursement> getByFarmerId(Long farmerId) {
        return disbursementRepo.findByFarmerId(farmerId);
    }

    public List<Disbursement> getByProgramId(int programId) {
        return disbursementRepo.findBySubsidyProgram_ProgramID(programId);
    }
}