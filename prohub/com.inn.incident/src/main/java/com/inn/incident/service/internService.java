package com.inn.incident.service;


import com.inn.incident.model.interns;
import com.inn.incident.repository.internsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class internService {

    @Autowired
    private internsRepo internRepository;

    public List<interns> getAllInterns() {
        return internRepository.findAll();
    }

    public interns addIntern(interns intern) {
        intern.setStatus("true"); // Ensure the default status is set to "true"
        return internRepository.save(intern);
    }

    public interns updateIntern(Integer id, interns updatedIntern) {
        Optional<interns> existingInternOpt = internRepository.findById(id);
        if (existingInternOpt.isPresent()) {
            interns existingIntern = existingInternOpt.get();
            existingIntern.setName(updatedIntern.getName());
            existingIntern.setMobile(updatedIntern.getMobile());
            existingIntern.setNic(updatedIntern.getNic());
            existingIntern.setEmail(updatedIntern.getEmail());
            existingIntern.setAddress(updatedIntern.getAddress());
            existingIntern.setStartDate(updatedIntern.getStartDate());
            existingIntern.setEndDate(updatedIntern.getEndDate());
            existingIntern.setInstitute(updatedIntern.getInstitute());
            existingIntern.setLanguagesKnown(updatedIntern.getLanguagesKnown());
            existingIntern.setField(updatedIntern.getField());
            existingIntern.setSupervisor(updatedIntern.getSupervisor());
            existingIntern.setAssignedWork(updatedIntern.getAssignedWork());
            existingIntern.setTargetDate(updatedIntern.getTargetDate());
            return internRepository.save(existingIntern);
        } else {
            throw new RuntimeException("Intern not found with id: " + id);
        }
    }

    public void softDeleteIntern(Integer id) {
        Optional<interns> existingInternOpt = internRepository.findById(id);
        if (existingInternOpt.isPresent()) {
            interns existingIntern = existingInternOpt.get();
            existingIntern.setStatus("false"); // Set status to "false" for soft delete
            internRepository.save(existingIntern);
        } else {
            throw new RuntimeException("Intern not found with id: " + id);
        }
    }
}
