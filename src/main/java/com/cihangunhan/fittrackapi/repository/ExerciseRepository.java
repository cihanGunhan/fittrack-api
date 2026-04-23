package com.cihangunhan.fittrackapi.repository;

import com.cihangunhan.fittrackapi.entity.Exercise;
import com.cihangunhan.fittrackapi.entity.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByMuscleGroupContainingIgnoreCase(String muscleGroup);
    List<Exercise> findByExerciseType(ExerciseType exerciseType);
    List<Exercise> findByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
}