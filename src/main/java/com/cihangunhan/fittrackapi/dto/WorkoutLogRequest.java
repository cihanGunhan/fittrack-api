package com.cihangunhan.fittrackapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WorkoutLogRequest {

    @NotNull(message = "Egzersiz ID boş olamaz")
    private Long exerciseId;

    @NotNull(message = "Tarih boş olamaz")
    private LocalDate logDate;

    private Integer sets;
    private Integer reps;
    private Double weight;
    private String notes;
}