package com.cihangunhan.fittrackapi.controller;

import com.cihangunhan.fittrackapi.dto.ExerciseRequest;
import com.cihangunhan.fittrackapi.dto.ExerciseResponse;
import com.cihangunhan.fittrackapi.entity.ExerciseType;
import com.cihangunhan.fittrackapi.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseResponse> createExercise(
            @Valid @RequestBody ExerciseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exerciseService.createExercise(request));
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponse>> getAllExercises() {
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getExerciseById(
            @PathVariable Long id) {
        return ResponseEntity.ok(exerciseService.getExerciseById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ExerciseResponse>> getByMuscleGroup(
            @RequestParam String muscleGroup) {
        return ResponseEntity.ok(
                exerciseService.getByMuscleGroup(muscleGroup));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ExerciseResponse>> getByType(
            @PathVariable ExerciseType type) {
        return ResponseEntity.ok(exerciseService.getByType(type));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponse> updateExercise(
            @PathVariable Long id,
            @Valid @RequestBody ExerciseRequest request) {
        return ResponseEntity.ok(
                exerciseService.updateExercise(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(
            @PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}