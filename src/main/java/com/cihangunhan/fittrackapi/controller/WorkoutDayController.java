package com.cihangunhan.fittrackapi.controller;

import com.cihangunhan.fittrackapi.dto.WorkoutDayRequest;
import com.cihangunhan.fittrackapi.dto.WorkoutDayResponse;
import com.cihangunhan.fittrackapi.service.WorkoutDayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-days")
@RequiredArgsConstructor
public class WorkoutDayController {

    private final WorkoutDayService workoutDayService;

    @PostMapping
    public ResponseEntity<WorkoutDayResponse> createDay(
            @Valid @RequestBody WorkoutDayRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutDayService.createDay(request));
    }

    @GetMapping("/plan/{planId}")
    public ResponseEntity<List<WorkoutDayResponse>> getDaysByPlan(
            @PathVariable Long planId) {
        return ResponseEntity.ok(
                workoutDayService.getDaysByPlan(planId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutDayResponse> getDayById(
            @PathVariable Long id) {
        return ResponseEntity.ok(workoutDayService.getDayById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutDayResponse> updateDay(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutDayRequest request) {
        return ResponseEntity.ok(
                workoutDayService.updateDay(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDay(@PathVariable Long id) {
        workoutDayService.deleteDay(id);
        return ResponseEntity.noContent().build();
    }
}