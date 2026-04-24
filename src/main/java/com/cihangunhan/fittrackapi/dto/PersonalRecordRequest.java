package com.cihangunhan.fittrackapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PersonalRecordRequest {

    @NotNull(message = "Egzersiz ID boş olamaz")
    private Long exerciseId;

    @NotNull
    @Positive(message = "Ağırlık pozitif olmalı")
    private Double weight;

    @NotNull
    @Positive(message = "Tekrar sayısı pozitif olmalı")
    private Integer reps;

    @NotNull(message = "Tarih boş olamaz")
    private LocalDate achievedDate;

    private String notes;
}