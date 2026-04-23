package com.cihangunhan.fittrackapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private String muscleGroup;

    private String description;

    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @ManyToMany(mappedBy = "exercises")
    private List<WorkoutDay> workoutDays = new ArrayList<>();
}