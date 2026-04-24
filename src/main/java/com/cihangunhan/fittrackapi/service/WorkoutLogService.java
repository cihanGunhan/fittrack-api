package com.cihangunhan.fittrackapi.service;

import com.cihangunhan.fittrackapi.config.CurrentUserProvider;
import com.cihangunhan.fittrackapi.dto.WorkoutLogRequest;
import com.cihangunhan.fittrackapi.dto.WorkoutLogResponse;
import com.cihangunhan.fittrackapi.entity.Exercise;
import com.cihangunhan.fittrackapi.entity.User;
import com.cihangunhan.fittrackapi.entity.WorkoutLog;
import com.cihangunhan.fittrackapi.exception.ResourceNotFoundException;
import com.cihangunhan.fittrackapi.exception.UnauthorizedException;
import com.cihangunhan.fittrackapi.repository.ExerciseRepository;
import com.cihangunhan.fittrackapi.repository.WorkoutLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;
    private final ExerciseRepository exerciseRepository;
    private final CurrentUserProvider currentUserProvider;

    public WorkoutLogResponse createLog(WorkoutLogRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        Exercise exercise = exerciseRepository
                .findById(request.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise", request.getExerciseId()));

        WorkoutLog log = WorkoutLog.builder()
                .user(currentUser)
                .exercise(exercise)
                .logDate(request.getLogDate())
                .sets(request.getSets())
                .reps(request.getReps())
                .weight(request.getWeight())
                .notes(request.getNotes())
                .build();

        return toResponse(workoutLogRepository.save(log));
    }

    public List<WorkoutLogResponse> getMyLogs() {
        Long userId = currentUserProvider.getCurrentUserId();
        return workoutLogRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<WorkoutLogResponse> getLogsByDate(LocalDate date) {
        Long userId = currentUserProvider.getCurrentUserId();
        return workoutLogRepository
                .findByUserIdAndLogDate(userId, date)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<WorkoutLogResponse> getLogsByExercise(Long exerciseId) {
        Long userId = currentUserProvider.getCurrentUserId();
        return workoutLogRepository
                .findByUserIdAndExerciseId(userId, exerciseId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WorkoutLogResponse getLogById(Long id) {
        return toResponse(findAndValidateOwnership(id));
    }

    public WorkoutLogResponse updateLog(Long id,
                                        WorkoutLogRequest request) {
        WorkoutLog log = findAndValidateOwnership(id);

        Exercise exercise = exerciseRepository
                .findById(request.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise", request.getExerciseId()));

        log.setExercise(exercise);
        log.setLogDate(request.getLogDate());
        log.setSets(request.getSets());
        log.setReps(request.getReps());
        log.setWeight(request.getWeight());
        log.setNotes(request.getNotes());

        return toResponse(workoutLogRepository.save(log));
    }

    public void deleteLog(Long id) {
        WorkoutLog log = findAndValidateOwnership(id);
        workoutLogRepository.delete(log);
    }

    private WorkoutLog findAndValidateOwnership(Long id) {
        WorkoutLog log = workoutLogRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkoutLog", id));

        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (!log.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedException(
                    "Bu kayda erişim yetkiniz yok");
        }

        return log;
    }

    private WorkoutLogResponse toResponse(WorkoutLog log) {
        WorkoutLogResponse response = new WorkoutLogResponse();
        response.setId(log.getId());
        response.setExerciseId(log.getExercise().getId());
        response.setExerciseName(log.getExercise().getName());
        response.setLogDate(log.getLogDate());
        response.setSets(log.getSets());
        response.setReps(log.getReps());
        response.setWeight(log.getWeight());
        response.setNotes(log.getNotes());
        return response;
    }
}