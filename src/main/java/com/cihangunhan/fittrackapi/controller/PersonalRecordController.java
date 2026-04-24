package com.cihangunhan.fittrackapi.controller;

import com.cihangunhan.fittrackapi.dto.PersonalRecordRequest;
import com.cihangunhan.fittrackapi.dto.PersonalRecordResponse;
import com.cihangunhan.fittrackapi.service.PersonalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/personal-records")
@RequiredArgsConstructor
public class PersonalRecordController {

    private final PersonalRecordService personalRecordService;

    @PostMapping
    public ResponseEntity<PersonalRecordResponse> createRecord(
            @Valid @RequestBody PersonalRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(personalRecordService.createRecord(request));
    }

    @GetMapping
    public ResponseEntity<List<PersonalRecordResponse>> getMyRecords() {
        return ResponseEntity.ok(
                personalRecordService.getMyRecords());
    }

    @GetMapping("/exercise/{exerciseId}")
    public ResponseEntity<List<PersonalRecordResponse>> getByExercise(
            @PathVariable Long exerciseId) {
        return ResponseEntity.ok(
                personalRecordService.getRecordsByExercise(exerciseId));
    }

    @GetMapping("/best/{exerciseId}")
    public ResponseEntity<Optional<PersonalRecordResponse>> getBest(
            @PathVariable Long exerciseId) {
        return ResponseEntity.ok(
                personalRecordService.getBestRecord(exerciseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        personalRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}