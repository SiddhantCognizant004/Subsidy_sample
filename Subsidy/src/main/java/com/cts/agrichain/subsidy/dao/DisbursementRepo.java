package com.cts.agrichain.subsidy.dao;

import com.cts.agrichain.subsidy.entity.Disbursement;
import com.cts.agrichain.subsidy.enums.DisbursementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisbursementRepo extends JpaRepository<Disbursement, Integer> {

    List<Disbursement> findByFarmerId(Long farmerId);

    List<Disbursement> findByDisbursementStatus(DisbursementStatus status);

    List<Disbursement> findBySubsidyProgram_ProgramID(int programID);

    // Check if a farmer has already applied for a specific subsidy program
    boolean existsByFarmerIdAndSubsidyProgram_ProgramID(Long farmerId, int programID);
}