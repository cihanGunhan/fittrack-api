package com.cihangunhan.fittrackapi.service;

import com.cihangunhan.fittrackapi.config.CurrentUserProvider;
import com.cihangunhan.fittrackapi.dto.PersonalRecordRequest;
import com.cihangunhan.fittrackapi.dto.PersonalRecordResponse;
import com.cihangunhan.fittrackapi.entity.Exercise;
import com.cihangunhan.fittrackapi.entity.PersonalRecord;
import com.cihangunhan.fittrackapi.entity.User;
import com.cihangunhan.fittrackapi.exception.ResourceNotFoundException;
import com.cihangunhan.fittrackapi.exception.UnauthorizedException;
import com.cihangunhan.fittrackapi.repository.ExerciseRepository;
import com.cihangunhan.fittrackapi.repository.PersonalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonalRecordService {

    private final PersonalRecordRepository personalRecordRepository;
    private final ExerciseRepository exerciseRepository;
    private final CurrentUserProvider currentUserProvider;

    public PersonalRecordResponse createRecord(
            PersonalRecordRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        Exercise exercise = exerciseRepository
                .findById(request.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Exercise", request.getExerciseId()));

        PersonalRecord record = PersonalRecord.builder()
                .user(currentUser)
                .exercise(exercise)
                .weight(request.getWeight())
                .reps(request.getReps())
                .achievedDate(request.getAchievedDate())
                .notes(request.getNotes())
                .build();

        return toResponse(personalRecordRepository.save(record));
    }

    public List<PersonalRecordResponse> getMyRecords() {
        Long userId = currentUserProvider.getCurrentUserId();
        return personalRecordRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PersonalRecordResponse> getRecordsByExercise(
            Long exerciseId) {
        Long userId = currentUserProvider.getCurrentUserId();
        return personalRecordRepository
                .findByUserIdAndExerciseId(userId, exerciseId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<PersonalRecordResponse> getBestRecord(
            Long exerciseId) {
        Long userId = currentUserProvider.getCurrentUserId();
        return personalRecordRepository
                .findTopByUserIdAndExerciseIdOrderByWeightDesc(
                        userId, exerciseId)
                .map(this::toResponse);
    }

    public void deleteRecord(Long id) {
        PersonalRecord record = findAndValidateOwnership(id);
        personalRecordRepository.delete(record);
    }

    private PersonalRecord findAndValidateOwnership(Long id) {
        PersonalRecord record = personalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "PersonalRecord", id));

        Long currentUserId = currentUserProvider.getCurrentUserId();
        if (!record.getUser().getId().equals(currentUserId)) {
            throw new UnauthorizedException(
                    "Bu kayda erişim yetkiniz yok");
        }

        return record;
    }

    private PersonalRecordResponse toResponse(PersonalRecord record) {
        PersonalRecordResponse response = new PersonalRecordResponse();
        response.setId(record.getId());
        response.setExerciseId(record.getExercise().getId());
        response.setExerciseName(record.getExercise().getName());
        response.setWeight(record.getWeight());
        response.setReps(record.getReps());
        response.setAchievedDate(record.getAchievedDate());
        response.setNotes(record.getNotes());
        return response;
    }
}