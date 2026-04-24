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
public class PersonalRecordResponse {

    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private Double weight;
    private Integer reps;
    private LocalDate achievedDate;
    private String notes;
}
