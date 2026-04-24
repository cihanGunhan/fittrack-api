package com.cihangunhan.fittrackapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutDayRequest {

    @NotBlank(message = "Gün adı boş olamaz")
    private String name;

    private Integer dayOrder;

    @NotNull(message = "Program ID boş olamaz")
    private Long workoutPlanId;
}