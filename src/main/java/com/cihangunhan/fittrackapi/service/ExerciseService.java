package com.cihangunhan.fittrackapi.service;

import com.cihangunhan.fittrackapi.dto.ExerciseRequest;
import com.cihangunhan.fittrackapi.dto.ExerciseResponse;
import com.cihangunhan.fittrackapi.entity.Exercise;
import com.cihangunhan.fittrackapi.entity.ExerciseType;
import com.cihangunhan.fittrackapi.exception.DuplicateResourceException;
import com.cihangunhan.fittrackapi.exception.ResourceNotFoundException;
import com.cihangunhan.fittrackapi.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public ExerciseResponse createExercise(ExerciseRequest request) {
        if (exerciseRepository.existsByName(request.getName())) {
            throw new DuplicateResourceException(
                    "Bu isimde egzersiz zaten mevcut: " + request.getName());
        }

        Exercise exercise = Exercise.builder()
                .name(request.getName())
                .muscleGroup(request.getMuscleGroup())
                .description(request.getDescription())
                .exerciseType(request.getExerciseType())
                .build();

        return toResponse(exerciseRepository.save(exercise));
    }

    public List<ExerciseResponse> getAllExercises() {
        return exerciseRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExerciseResponse getExerciseById(Long id) {
        return toResponse(findById(id));
    }

    public List<ExerciseResponse> getByMuscleGroup(String muscleGroup) {
        return exerciseRepository
                .findByMuscleGroupContainingIgnoreCase(muscleGroup)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ExerciseResponse> getByType(ExerciseType type) {
        return exerciseRepository.findByExerciseType(type)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ExerciseResponse> searchByName(String name) {
        return exerciseRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExerciseResponse updateExercise(Long id,
                                           ExerciseRequest request) {
        Exercise exercise = findById(id);
        exercise.setName(request.getName());
        exercise.setMuscleGroup(request.getMuscleGroup());
        exercise.setDescription(request.getDescription());
        exercise.setExerciseType(request.getExerciseType());
        return toResponse(exerciseRepository.save(exercise));
    }

    public void deleteExercise(Long id) {
        findById(id);
        exerciseRepository.deleteById(id);
    }

    private Exercise findById(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Exercise", id));
    }

    private ExerciseResponse toResponse(Exercise exercise) {
        ExerciseResponse response = new ExerciseResponse();
        response.setId(exercise.getId());
        response.setName(exercise.getName());
        response.setMuscleGroup(exercise.getMuscleGroup());
        response.setDescription(exercise.getDescription());
        response.setExerciseType(exercise.getExerciseType());
        return response;
    }
}