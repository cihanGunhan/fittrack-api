package com.cihangunhan.fittrackapi;

import com.cihangunhan.fittrackapi.config.CurrentUserProvider;
import com.cihangunhan.fittrackapi.dto.WorkoutPlanRequest;
import com.cihangunhan.fittrackapi.dto.WorkoutPlanResponse;
import com.cihangunhan.fittrackapi.entity.Role;
import com.cihangunhan.fittrackapi.entity.User;
import com.cihangunhan.fittrackapi.entity.WorkoutPlan;
import com.cihangunhan.fittrackapi.exception.ResourceNotFoundException;
import com.cihangunhan.fittrackapi.exception.UnauthorizedException;
import com.cihangunhan.fittrackapi.repository.WorkoutPlanRepository;
import com.cihangunhan.fittrackapi.service.WorkoutPlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkoutPlanServiceTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    private User testUser;
    private User otherUser;
    private WorkoutPlan testPlan;
    private WorkoutPlanRequest testRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .fullName("Cihan Günhan")
                .email("cihan@fittrack.com")
                .password("encoded")
                .role(Role.USER)
                .build();

        otherUser = User.builder()
                .id(2L)
                .fullName("Başka Kullanıcı")
                .email("other@fittrack.com")
                .password("encoded")
                .role(Role.USER)
                .build();

        testPlan = WorkoutPlan.builder()
                .id(1L)
                .name("Push Pull Legs")
                .description("6 günlük split")
                .user(testUser)
                .workoutDays(new ArrayList<>())
                .build();

        testRequest = new WorkoutPlanRequest();
        testRequest.setName("Push Pull Legs");
        testRequest.setDescription("6 günlük split");
    }

    @Test
    @DisplayName("Program başarıyla oluşturulur")
    void createPlan_ShouldCreateAndReturnPlan() {
        when(currentUserProvider.getCurrentUser())
                .thenReturn(testUser);
        when(workoutPlanRepository.save(any(WorkoutPlan.class)))
                .thenReturn(testPlan);

        WorkoutPlanResponse result =
                workoutPlanService.createPlan(testRequest);

        assertThat(result.getName()).isEqualTo("Push Pull Legs");
        assertThat(result.getUserId()).isEqualTo(1L);
        verify(workoutPlanRepository, times(1))
                .save(any(WorkoutPlan.class));
    }

    @Test
    @DisplayName("Kullanıcının programları listelenir")
    void getMyPlans_ShouldReturnUserPlans() {
        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(workoutPlanRepository.findByUserId(1L))
                .thenReturn(List.of(testPlan));

        List<WorkoutPlanResponse> result =
                workoutPlanService.getMyPlans();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName())
                .isEqualTo("Push Pull Legs");
        verify(workoutPlanRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("ID ile program getirilir")
    void getPlanById_WhenOwner_ShouldReturnPlan() {
        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(workoutPlanRepository.findById(1L))
                .thenReturn(Optional.of(testPlan));

        WorkoutPlanResponse result =
                workoutPlanService.getPlanById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Push Pull Legs");
    }

    @Test
    @DisplayName("Başka kullanıcının programına erişince hata fırlatılır")
    void getPlanById_WhenNotOwner_ShouldThrowUnauthorized() {
        when(currentUserProvider.getCurrentUserId())
                .thenReturn(2L);
        when(workoutPlanRepository.findById(1L))
                .thenReturn(Optional.of(testPlan));

        assertThatThrownBy(() ->
                workoutPlanService.getPlanById(1L))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    @DisplayName("Olmayan program getirilince hata fırlatılır")
    void getPlanById_WhenNotExists_ShouldThrowNotFound() {
        when(workoutPlanRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                workoutPlanService.getPlanById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Program başarıyla güncellenir")
    void updatePlan_WhenOwner_ShouldUpdatePlan() {
        WorkoutPlanRequest updateRequest = new WorkoutPlanRequest();
        updateRequest.setName("Yeni Program");
        updateRequest.setDescription("Yeni açıklama");

        WorkoutPlan updatedPlan = WorkoutPlan.builder()
                .id(1L)
                .name("Yeni Program")
                .description("Yeni açıklama")
                .user(testUser)
                .workoutDays(new ArrayList<>())
                .build();

        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(workoutPlanRepository.findById(1L))
                .thenReturn(Optional.of(testPlan));
        when(workoutPlanRepository.save(any(WorkoutPlan.class)))
                .thenReturn(updatedPlan);

        WorkoutPlanResponse result =
                workoutPlanService.updatePlan(1L, updateRequest);

        assertThat(result.getName()).isEqualTo("Yeni Program");
        verify(workoutPlanRepository, times(1))
                .save(any(WorkoutPlan.class));
    }

    @Test
    @DisplayName("Program başarıyla silinir")
    void deletePlan_WhenOwner_ShouldDeletePlan() {
        when(currentUserProvider.getCurrentUserId())
                .thenReturn(1L);
        when(workoutPlanRepository.findById(1L))
                .thenReturn(Optional.of(testPlan));

        workoutPlanService.deletePlan(1L);

        verify(workoutPlanRepository, times(1)).delete(testPlan);
    }

    @Test
    @DisplayName("Başka kullanıcının programı silinince hata fırlatılır")
    void deletePlan_WhenNotOwner_ShouldThrowUnauthorized() {
        when(currentUserProvider.getCurrentUserId())
                .thenReturn(2L);
        when(workoutPlanRepository.findById(1L))
                .thenReturn(Optional.of(testPlan));

        assertThatThrownBy(() ->
                workoutPlanService.deletePlan(1L))
                .isInstanceOf(UnauthorizedException.class);

        verify(workoutPlanRepository, never())
                .delete(any(WorkoutPlan.class));
    }
}