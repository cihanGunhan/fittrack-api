package com.cihangunhan.fittrackapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanResponse {

    private Long id;
    private String name;
    private String description;
    private Long userId;
    private int dayCount;
}