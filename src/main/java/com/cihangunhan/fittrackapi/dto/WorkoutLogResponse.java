package com.cihangunhan.fittrackapi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogResponse {

    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private LocalDate logDate;
    private Integer sets;
    private Integer reps;
    private Double weight;
    private String notes;
}