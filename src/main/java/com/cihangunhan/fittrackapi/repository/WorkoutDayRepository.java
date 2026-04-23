package com.cihangunhan.fittrackapi.repository;

import com.cihangunhan.fittrackapi.entity.WorkoutDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutDayRepository extends JpaRepository<WorkoutDay, Long> {
    List<WorkoutDay> findByWorkoutPlanId(Long workoutPlanId);
}