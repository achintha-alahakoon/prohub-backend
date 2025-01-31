package com.inn.incident.controller;

import com.inn.incident.model.Applicant;
import com.inn.incident.service.ApplicantService;
import org.apache.commons.io.FilenameUtils;
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
    @RequestMapping("/applicants")
    public class ApplicantController {

        private final ApplicantService applicantService;

        public ApplicantController(ApplicantService applicantService) {
            this.applicantService = applicantService;
        }

        @PostMapping("/upload")
        public ResponseEntity<String> uploadCV(
                @RequestParam("firstName") String firstName,
                @RequestParam("lastName") String lastName,
                @RequestParam("confirmEmail") String confirmEmail,
                @RequestParam("placeOfResidence") String placeOfResidence,
                @RequestParam("messageToHR") String messageToHR,
                @RequestParam("phone") String phone,
                @RequestParam("cv") MultipartFile cv) {

            if (firstName.isEmpty() || lastName.isEmpty() || confirmEmail.isEmpty() ||
                    phone.isEmpty() || placeOfResidence.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid input data. All fields are required.");
            }

            try {
                String filePath = saveCVFile(cv);
                Applicant applicant = new Applicant();
                applicant.setFirstName(firstName);
                applicant.setLastName(lastName);
                applicant.setConfirmEmail(confirmEmail);
                applicant.setPlaceOfResidence(placeOfResidence);
                applicant.setMessageToHR(messageToHR);
                applicant.setPhone(phone);
                applicant.setCvFilePath(filePath);

                applicantService.saveApplicant(applicant);
                return ResponseEntity.ok("CV uploaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading CV.");
            }
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
