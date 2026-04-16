package com.cts.agrichain.subsidy.controller;

import com.cts.agrichain.subsidy.entity.SubsidyProgram;
import com.cts.agrichain.subsidy.service.SubsidyProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subsidy-programs")
public class SubsidyProgramController {

    @Autowired
    private SubsidyProgramService subsidyProgramService;

    @PostMapping
    public SubsidyProgram create(@RequestBody SubsidyProgram program) {
        return subsidyProgramService.create(program);
    }

    @GetMapping("/{id}")
    public SubsidyProgram getById(@PathVariable Integer id) {
        return subsidyProgramService.findById(id);
    }

    @GetMapping
    public List<SubsidyProgram> getAll() {
        return subsidyProgramService.getAll();
    }

    @PutMapping("/{id}")
    public SubsidyProgram update(@PathVariable Integer id, @RequestBody SubsidyProgram updatedProgram) {
        return subsidyProgramService.update(id, updatedProgram);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        subsidyProgramService.delete(id);
        return "Subsidy Program deleted successfully";
    }

    @GetMapping("/title/{title}")
    public List<SubsidyProgram> getByTitle(@PathVariable String title) {
        return subsidyProgramService.getProgramsByTitle(title);
    }

    @GetMapping("/allotted-budget/{budget}")
    public List<SubsidyProgram> getByAllottedBudget(@PathVariable double budget) {
        return subsidyProgramService.getByAllottedBudget(budget);
    }
}