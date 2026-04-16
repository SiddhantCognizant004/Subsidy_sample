package com.cts.agrichain.subsidy.service;

import com.cts.agrichain.subsidy.dao.SubsidyProgramRepo;
import com.cts.agrichain.subsidy.entity.SubsidyProgram;
import com.cts.agrichain.subsidy.enums.SubsidyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubsidyProgramService {

    @Autowired
    private SubsidyProgramRepo subsidyProgramRepo;

    public SubsidyProgram create(SubsidyProgram program) {
        // Always initialize consumedBudget to 0.0 on creation
        program.setConsumedBudget(0.0);
        // Always set status to PENDING on creation
        program.setSubsidyStatus(SubsidyStatus.PENDING);
        return subsidyProgramRepo.save(program);
    }

    public SubsidyProgram findById(Integer id) {
        return subsidyProgramRepo.findById(id).orElse(null);
    }

    public List<SubsidyProgram> getAll() {
        return subsidyProgramRepo.findAll();
    }

    public SubsidyProgram update(Integer id, SubsidyProgram updatedProgram) {
        return subsidyProgramRepo.findById(id).map(program -> {
            program.setTitle(updatedProgram.getTitle());
            program.setDescription(updatedProgram.getDescription());
            program.setStartDate(updatedProgram.getStartDate());
            program.setEndDate(updatedProgram.getEndDate());
            program.setAllottedBudget(updatedProgram.getAllottedBudget());
            program.setConsumedBudget(updatedProgram.getConsumedBudget());
            program.setSubsidyStatus(updatedProgram.getSubsidyStatus());
            return subsidyProgramRepo.save(program);
        }).orElse(null);
    }

    public void delete(Integer id) {
        subsidyProgramRepo.deleteById(id);
    }

    public List<SubsidyProgram> getProgramsByTitle(String title) {
        return subsidyProgramRepo.findByTitle(title);
    }

    public List<SubsidyProgram> getByAllottedBudget(double budget) {
        return subsidyProgramRepo.findByAllottedBudget(budget);
    }
}