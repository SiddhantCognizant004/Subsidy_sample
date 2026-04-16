package com.cts.agrichain.subsidy.entity;

import com.cts.agrichain.subsidy.enums.SubsidyStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "subsidy_program")
public class SubsidyProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer programID;

    private String title;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double allottedBudget;

    private Double consumedBudget;

    @Enumerated(EnumType.STRING)
    private SubsidyStatus subsidyStatus;

    @OneToMany(mappedBy = "subsidyProgram", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Disbursement> disbursements;

    public Integer getProgramID() {
        return programID;
    }

    public void setProgramID(Integer programID) {
        this.programID = programID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getAllottedBudget() {
        return allottedBudget;
    }

    public void setAllottedBudget(Double allottedBudget) {
        this.allottedBudget = allottedBudget;
    }

    public Double getConsumedBudget() {
        return consumedBudget;
    }

    public void setConsumedBudget(Double consumedBudget) {
        this.consumedBudget = consumedBudget;
    }

    public SubsidyStatus getSubsidyStatus() {
        return subsidyStatus;
    }

    public void setSubsidyStatus(SubsidyStatus subsidyStatus) {
        this.subsidyStatus = subsidyStatus;
    }

    public List<Disbursement> getDisbursements() {
        return disbursements;
    }

    public void setDisbursements(List<Disbursement> disbursements) {
        this.disbursements = disbursements;
    }
}