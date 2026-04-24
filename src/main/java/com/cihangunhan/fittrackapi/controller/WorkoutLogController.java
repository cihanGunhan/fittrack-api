package com.cihangunhan.fittrackapi.controller;

import com.cihangunhan.fittrackapi.dto.WorkoutLogRequest;
import com.cihangunhan.fittrackapi.dto.WorkoutLogResponse;
import com.cihangunhan.fittrackapi.service.WorkoutLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workout-logs")
@RequiredArgsConstructor
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;

    @PostMapping
    public ResponseEntity<WorkoutLogResponse> createLog(
            @Valid @RequestBody WorkoutLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutLogService.createLog(request));
    }

    @GetMapping
    public ResponseEntity<List<WorkoutLogResponse>> getMyLogs() {
        return ResponseEntity.ok(workoutLogService.getMyLogs());
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<WorkoutLogResponse>> getLogsByDate(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        return ResponseEntity.ok(
                workoutLogService.getLogsByDate(date));
    }

    @GetMapping("/exercise/{exerciseId}")
    public ResponseEntity<List<WorkoutLogResponse>> getLogsByExercise(
            @PathVariable Long exerciseId) {
        return ResponseEntity.ok(
                workoutLogService.getLogsByExercise(exerciseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutLogResponse> getLogById(
            @PathVariable Long id) {
        return ResponseEntity.ok(workoutLogService.getLogById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutLogResponse> updateLog(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutLogRequest request) {
        return ResponseEntity.ok(
                workoutLogService.updateLog(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        workoutLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }
}