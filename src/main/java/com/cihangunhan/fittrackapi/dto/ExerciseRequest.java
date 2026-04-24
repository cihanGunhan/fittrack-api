package com.cihangunhan.fittrackapi.dto;

import com.cihangunhan.fittrackapi.entity.ExerciseType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter


public class ExerciseRequest {

    @NotBlank(message = "Egzersiz Adı Boş Olamaz")
     private String name;
     private String muscleGroup;
     private String description;
     private ExerciseType exerciseType;

}
