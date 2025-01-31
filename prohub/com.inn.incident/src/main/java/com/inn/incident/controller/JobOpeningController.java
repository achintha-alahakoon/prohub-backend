//package com.inn.incident.controller;
//
//import com.inn.incident.model.Applicant;
//import com.inn.incident.model.JobOpening;
//import com.inn.incident.service.JobOpeningService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/jobs")
//public class JobOpeningController {
//
//    @Autowired
//    private JobOpeningService jobOpeningService;
//
//    @PostMapping
//    public ResponseEntity<JobOpening> createJob(@RequestBody JobOpening jobOpening) {
//        return ResponseEntity.ok(jobOpeningService.saveJob(jobOpening));
//    }
//
//    @GetMapping
//    public List<JobOpening> getAllJobs() {
//        return jobOpeningService.getAllJobs();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<JobOpening> getJobById(@PathVariable Long id) {
//        return jobOpeningService.getJobById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<JobOpening> updateJob(@PathVariable Long id, @RequestBody JobOpening updatedJob) {
//        return jobOpeningService.getJobById(id)
//                .map(job -> {
//                    job.setTitle(updatedJob.getTitle());
//                    job.setDescription(updatedJob.getDescription());
//                    job.setRequirements(updatedJob.getRequirements());
//                    job.setLocation(updatedJob.getLocation());
//                    job.setInternshipDuration(updatedJob.getInternshipDuration());
//                    return ResponseEntity.ok(jobOpeningService.saveJob(job));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
//        if (jobOpeningService.getJobById(id).isPresent()) {
//            jobOpeningService.deleteJob(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//
//    @GetMapping("/{id}/applicants")
//    public ResponseEntity<List<Applicant>> getApplicantsForJob(@PathVariable Long id) {
//        return jobOpeningService.getJobById(id)
//                .map(jobOpening -> ResponseEntity.ok(jobOpening.getApplicants()))
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//
//    @PostMapping("/{jobId}/applicants")
//    public ResponseEntity<String> addApplicantToJob(@PathVariable Long jobId, @RequestBody Applicant applicant) {
//        if (jobOpeningService.getJobById(jobId).isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        applicantService.saveApplicant(applicant);
//        return ResponseEntity.ok("Applicant added successfully.");
//    }
//
//}

package com.inn.incident.controller;

import com.inn.incident.model.Applicant;
import com.inn.incident.model.JobOpening;
import com.inn.incident.service.ApplicantService;
import com.inn.incident.service.JobOpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobOpeningController {

    private final JobOpeningService jobOpeningService;
    private final ApplicantService applicantService;

    @Autowired
    public JobOpeningController(JobOpeningService jobOpeningService, ApplicantService applicantService) {
        this.jobOpeningService = jobOpeningService;
        this.applicantService = applicantService;
    }

    @PostMapping
    public ResponseEntity<JobOpening> createJob(@RequestBody JobOpening jobOpening) {
        return ResponseEntity.ok(jobOpeningService.saveJob(jobOpening));
    }

    @GetMapping
    public List<JobOpening> getAllJobs() {
        return jobOpeningService.getAllJobs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobOpening> getJobById(@PathVariable Long id) {
        return jobOpeningService.getJobById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobOpening> updateJob(@PathVariable Long id, @RequestBody JobOpening updatedJob) {
        return jobOpeningService.getJobById(id)
                .map(job -> {
                    job.setTitle(updatedJob.getTitle());
                    job.setDescription(updatedJob.getDescription());
                    job.setRequirements(updatedJob.getRequirements());
                    job.setLocation(updatedJob.getLocation());
                    job.setInternshipDuration(updatedJob.getInternshipDuration());
                    return ResponseEntity.ok(jobOpeningService.saveJob(job));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        if (jobOpeningService.getJobById(id).isPresent()) {
            jobOpeningService.deleteJob(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{jobId}/applicants")
    public ResponseEntity<String> addApplicantToJob(@PathVariable Long jobId, @RequestBody Applicant applicant) {
        if (jobOpeningService.getJobById(jobId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        applicantService.saveApplicant(applicant);  // Now just saves the applicant without linking to a job
        return ResponseEntity.ok("Applicant added successfully.");
    }

}

