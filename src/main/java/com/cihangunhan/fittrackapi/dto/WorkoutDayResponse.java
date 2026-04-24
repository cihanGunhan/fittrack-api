package com.cihangunhan.fittrackapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDayResponse {
    private Long id;
    private String name;
    private Integer dayOrder;
    private Long workoutPlanId;
    private Integer exerciseCount;
}