package com.cihangunhan.fittrackapi.service;

import com.cihangunhan.fittrackapi.config.CurrentUserProvider;
import com.cihangunhan.fittrackapi.dto.WorkoutPlanRequest;
import com.cihangunhan.fittrackapi.dto.WorkoutPlanResponse;
import com.cihangunhan.fittrackapi.entity.User;
import com.cihangunhan.fittrackapi.entity.WorkoutPlan;
import com.cihangunhan.fittrackapi.exception.ResourceNotFoundException;
import com.cihangunhan.fittrackapi.exception.UnauthorizedException;
import com.cihangunhan.fittrackapi.repository.WorkoutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutPlanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final CurrentUserProvider currentUserProvider;

    public WorkoutPlanResponse createPlan(WorkoutPlanRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        WorkoutPlan plan = WorkoutPlan.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(currentUser)
                .build();

        return toResponse(workoutPlanRepository.save(plan));
    }

    public List<WorkoutPlanResponse> getMyPlans() {
        Long userId = currentUserProvider.getCurrentUserId();
        return workoutPlanRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WorkoutPlanResponse getPlanById(Long id) {
        WorkoutPlan plan = findAndValidateOwnership(id);
        return toResponse(plan);
    }

    public WorkoutPlanResponse updatePlan(Long id,
                                          WorkoutPlanRequest request) {
        WorkoutPlan plan = findAndValidateOwnership(id);
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        return toResponse(workoutPlanRepository.save(plan));
    }

    public void deletePlan(Long id) {
        WorkoutPlan plan = findAndValidateOwnership(id);
        workoutPlanRepository.delete(plan);
    }

    private WorkoutPlan findAndValidateOwnership(Long id) {
        WorkoutPlan plan = workoutPlanRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("WorkoutPlan", id));

        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (!plan.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedException(
                    "Bu programa erişim yetkiniz yok");
        }

        return plan;
    }

    private WorkoutPlanResponse toResponse(WorkoutPlan plan) {
        return new WorkoutPlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                plan.getUser().getId(),
                plan.getWorkoutDays().size()
        );
    }
}