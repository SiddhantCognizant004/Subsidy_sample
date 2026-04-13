package com.cts.agrichain.subsidy.dao;

import com.cts.agrichain.subsidy.entity.SubsidyProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubsidyProgramRepo extends JpaRepository<SubsidyProgram, Integer> {

    List<SubsidyProgram> findByTitle(String title);

    List<SubsidyProgram> findByAllottedBudget(double allottedBudget);

    List<SubsidyProgram> findByConsumedBudget(double consumedBudget);

    List<SubsidyProgram> findByProgramID(int programID);
}