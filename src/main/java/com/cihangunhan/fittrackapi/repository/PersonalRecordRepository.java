package com.cihangunhan.fittrackapi.repository;

import com.cihangunhan.fittrackapi.entity.PersonalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalRecordRepository
        extends JpaRepository<PersonalRecord, Long> {
    List<PersonalRecord> findByUserId(Long userId);
    List<PersonalRecord> findByUserIdAndExerciseId(
            Long userId, Long exerciseId);
    Optional<PersonalRecord> findTopByUserIdAndExerciseIdOrderByWeightDesc(
            Long userId, Long exerciseId);
}