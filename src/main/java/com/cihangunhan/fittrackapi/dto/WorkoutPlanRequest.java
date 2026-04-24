package com.cihangunhan.fittrackapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutPlanRequest {

    @NotBlank(message = "Program adı boş olamaz")
    private String name;

    private String description;
}