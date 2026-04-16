package com.cts.agrichain.subsidy.entity;

import com.cts.agrichain.subsidy.enums.DisbursementStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "disbursement")
public class Disbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer disbursementID;

    // ONLY store farmerId (no Farmer entity)
    private Long farmerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programid")
    private SubsidyProgram subsidyProgram;

    private Double disbursementAmount;

    private LocalDate disbursementDate;

    @Enumerated(EnumType.STRING)
    private DisbursementStatus disbursementStatus;

    // Getters and Setters

    public Integer getDisbursementID() {
        return disbursementID;
    }

    public void setDisbursementID(Integer disbursementID) {
        this.disbursementID = disbursementID;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public SubsidyProgram getSubsidyProgram() {
        return subsidyProgram;
    }

    public void setSubsidyProgram(SubsidyProgram subsidyProgram) {
        this.subsidyProgram = subsidyProgram;
    }

    public Double getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(Double disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public DisbursementStatus getDisbursementStatus() {
        return disbursementStatus;
    }

    public void setDisbursementStatus(DisbursementStatus disbursementStatus) {
        this.disbursementStatus = disbursementStatus;
    }
}