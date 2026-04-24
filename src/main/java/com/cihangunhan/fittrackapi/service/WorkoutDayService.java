package com.cihangunhan.fittrackapi.service;

import com.cihangunhan.fittrackapi.config.CurrentUserProvider;
import com.cihangunhan.fittrackapi.dto.WorkoutDayRequest;
import com.cihangunhan.fittrackapi.dto.WorkoutDayResponse;
import com.cihangunhan.fittrackapi.entity.WorkoutDay;
import com.cihangunhan.fittrackapi.entity.WorkoutPlan;
import com.cihangunhan.fittrackapi.exception.ResourceNotFoundException;
import com.cihangunhan.fittrackapi.exception.UnauthorizedException;
import com.cihangunhan.fittrackapi.repository.WorkoutDayRepository;
import com.cihangunhan.fittrackapi.repository.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutDayService {

    private final WorkoutDayRepository workoutDayRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final CurrentUserProvider currentUserProvider;

    public WorkoutDayResponse createDay(WorkoutDayRequest request) {
        WorkoutPlan plan = workoutPlanRepository
                .findById(request.getWorkoutPlanId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WorkoutPlan", request.getWorkoutPlanId()));

        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (!plan.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedException(
                    "Bu programa erişim yetkiniz yok");
        }

        WorkoutDay day = WorkoutDay.builder()
                .name(request.getName())
                .dayOrder(request.getDayOrder())
                .workoutPlan(plan)
                .build();

        return toResponse(workoutDayRepository.save(day));
    }

    public List<WorkoutDayResponse> getDaysByPlan(Long planId) {
        WorkoutPlan plan = workoutPlanRepository.findById(planId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "WorkoutPlan", planId));

        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (!plan.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedException(
                    "Bu programa erişim yetkiniz yok");
        }

        return workoutDayRepository.findByWorkoutPlanId(planId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WorkoutDayResponse getDayById(Long id) {
        return toResponse(findAndValidateOwnership(id));
    }

    public WorkoutDayResponse updateDay(Long id,
                                        WorkoutDayRequest request) {
        WorkoutDay day = findAndValidateOwnership(id);
        day.setName(request.getName());
        day.setDayOrder(request.getDayOrder());
        return toResponse(workoutDayRepository.save(day));
    }

    public void deleteDay(Long id) {
        WorkoutDay day = findAndValidateOwnership(id);
        workoutDayRepository.delete(day);
    }

    private WorkoutDay findAndValidateOwnership(Long id) {
        WorkoutDay day = workoutDayRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkoutDay", id));

        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (!day.getWorkoutPlan().getUser().getId()
                .equals(currentUserId)) {
            throw new UnauthorizedException(
                    "Bu güne erişim yetkiniz yok");
        }

        return day;
    }

    private WorkoutDayResponse toResponse(WorkoutDay day) {
        WorkoutDayResponse response = new WorkoutDayResponse();
        response.setId(day.getId());
        response.setName(day.getName());
        response.setDayOrder(day.getDayOrder());
        response.setWorkoutPlanId(day.getWorkoutPlan().getId());
        response.setExerciseCount(day.getExercises().size());
        return response;
    }
}