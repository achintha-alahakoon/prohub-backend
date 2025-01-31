package com.inn.incident.service;

import com.inn.incident.model.JobOpening;
import com.inn.incident.repository.JobOpeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobOpeningService {

    @Autowired
    private JobOpeningRepository jobOpeningRepository;

    public JobOpening saveJob(JobOpening jobOpening) {
        return jobOpeningRepository.save(jobOpening);
    }

    public List<JobOpening> getAllJobs() {
        return jobOpeningRepository.findAll();
    }

    public Optional<JobOpening> getJobById(Long id) {
        return jobOpeningRepository.findById(id);
    }

    public void deleteJob(Long id) {
        jobOpeningRepository.deleteById(id);
    }
}
