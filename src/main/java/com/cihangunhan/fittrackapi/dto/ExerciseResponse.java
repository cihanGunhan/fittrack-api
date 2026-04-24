package com.cihangunhan.fittrackapi.dto;

import com.cihangunhan.fittrackapi.entity.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResponse {

    private Long id;
    private String name;
    private String muscleGroup;
    private String description;
    private ExerciseType exerciseType;
}