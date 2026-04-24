package com.cihangunhan.fittrackapi.controller;

import com.cihangunhan.fittrackapi.dto.WorkoutPlanRequest;
import com.cihangunhan.fittrackapi.dto.WorkoutPlanResponse;
import com.cihangunhan.fittrackapi.service.WorkoutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @PostMapping
    public ResponseEntity<WorkoutPlanResponse> createPlan(
            @Valid @RequestBody WorkoutPlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutPlanService.createPlan(request));
    }

    @GetMapping
    public ResponseEntity<List<WorkoutPlanResponse>> getMyPlans() {
        return ResponseEntity.ok(workoutPlanService.getMyPlans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> getPlanById(
            @PathVariable Long id) {
        return ResponseEntity.ok(workoutPlanService.getPlanById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutPlanResponse> updatePlan(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutPlanRequest request) {
        return ResponseEntity.ok(
                workoutPlanService.updatePlan(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        workoutPlanService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }
}