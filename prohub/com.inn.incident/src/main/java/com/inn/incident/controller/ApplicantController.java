package com.inn.incident.controller;

import com.inn.incident.model.Applicant;
import com.inn.incident.model.JobOpening;
import com.inn.incident.service.ApplicantService;
import com.inn.incident.service.EmailService;
import com.inn.incident.service.JobOpeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private JobOpeningService jobOpeningService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCV(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("confirmEmail") String confirmEmail,
            @RequestParam("placeOfResidence") String placeOfResidence,
            @RequestParam("messageToHR") String messageToHR,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("cv") MultipartFile cv,
            @RequestParam("jobOpeningId") Long jobOpeningId) {


        if (firstName.isEmpty() || lastName.isEmpty() || confirmEmail.isEmpty() ||
                email.isEmpty() || phone.isEmpty() || placeOfResidence.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input data. All fields are required.");
        }

        if (!isValidEmail(email) || !isValidEmail(confirmEmail)) {
            return ResponseEntity.badRequest().body("Invalid email format.");
        }

        if (!email.equals(confirmEmail)) {
            return ResponseEntity.badRequest().body("Email and Confirm Email do not match.");
        }

        try {

            Optional<JobOpening> jobOpeningOpt = jobOpeningService.getJobById(jobOpeningId);
            if (jobOpeningOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid job opening ID.");
            }

            String filePath = saveCVFile(cv);
            Applicant applicant = new Applicant();
            applicant.setFirstName(firstName);
            applicant.setLastName(lastName);
            applicant.setConfirmEmail(confirmEmail);
            applicant.setPlaceOfResidence(placeOfResidence);
            applicant.setMessageToHR(messageToHR);
            applicant.setEmail(email);
            applicant.setPhone(phone);
            applicant.setCvFilePath(filePath);
            applicant.setJobOpening(jobOpeningOpt.get());

            applicantService.saveApplicant(applicant);
            emailService.sendApplicationReceivedEmail(email, firstName + " " + lastName);

            return ResponseEntity.ok("Application received! An acknowledgment email has been sent.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading CV.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    private String saveCVFile(MultipartFile cv) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String sanitizedFileName = StringUtils.cleanPath(cv.getOriginalFilename());
        String filePath = uploadDir + System.currentTimeMillis() + "_" + sanitizedFileName;
        File file = new File(filePath);
        cv.transferTo(file);
        return filePath;
    }

    @GetMapping
    public List<Applicant> getAllApplicants() {
        return applicantService.getAllApplicants();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadCV(@PathVariable Long id) {
        Optional<Applicant> applicantOpt = applicantService.getApplicantById(id);
        if (applicantOpt.isPresent()) {
            Applicant applicant = applicantOpt.get();
            File file = new File(applicant.getCvFilePath());
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Resource resource = new FileSystemResource(file);
            try {
                String contentType = Files.probeContentType(file.toPath());
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                        .body(resource);
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
