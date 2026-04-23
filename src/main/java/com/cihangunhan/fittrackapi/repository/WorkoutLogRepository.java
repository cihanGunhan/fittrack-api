package com.cihangunhan.fittrackapi.repository;

import com.cihangunhan.fittrackapi.entity.WorkoutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {
    List<WorkoutLog> findByUserId(Long userId);
    List<WorkoutLog> findByUserIdAndLogDate(Long userId, LocalDate logDate);
    List<WorkoutLog> findByUserIdAndExerciseId(Long userId, Long exerciseId);
    List<WorkoutLog> findByUserIdAndLogDateBetween(
            Long userId, LocalDate startDate, LocalDate endDate);
}