package com.powersphere.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.powersphere.notification.dto.request.CreateNotificationRequest;
import com.powersphere.notification.dto.request.UpdateNotificationRequest;
import com.powersphere.notification.dto.response.ApiResponse;
import com.powersphere.notification.dto.response.NotificationResponse;
import com.powersphere.notification.dto.response.PagedResponse;
import com.powersphere.notification.exception.NotificationNotFoundException;
import com.powersphere.notification.model.NotificationChannel;
import com.powersphere.notification.model.NotificationPriority;
import com.powersphere.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        NotificationController controller = new NotificationController(notificationService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createNotification_ShouldReturn201() throws Exception {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .title("Test Notification")
                .message("Test message")
                .notificationType(NotificationChannel.EMAIL)
                .priority(NotificationPriority.HIGH)
                .channel(NotificationChannel.EMAIL)
                .recipientUser(1L)
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .title("Test Notification")
                .message("Test message")
                .build();

        when(notificationService.createNotification(any(CreateNotificationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("Test Notification"));
    }

    @Test
    void getNotificationById_ShouldReturn200() throws Exception {
        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .title("Test")
                .build();

        when(notificationService.getNotificationById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1));
    }

    @Test
    void getNotificationById_ShouldReturn404_WhenNotFound() throws Exception {
        when(notificationService.getNotificationById(999L))
                .thenThrow(new NotificationNotFoundException(999L));

        mockMvc.perform(get("/api/v1/notifications/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateNotification_ShouldReturn200() throws Exception {
        UpdateNotificationRequest request = UpdateNotificationRequest.builder()
                .title("Updated")
                .build();

        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .title("Updated")
                .build();

        when(notificationService.updateNotification(anyLong(), any(UpdateNotificationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/v1/notifications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void deleteNotification_ShouldReturn200() throws Exception {
        mockMvc.perform(delete("/api/v1/notifications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getAllNotifications_ShouldReturn200() throws Exception {
        PagedResponse<NotificationResponse> pagedResponse = PagedResponse.<NotificationResponse>builder()
                .content(Collections.emptyList())
                .page(0)
                .size(10)
                .totalElements(0)
                .totalPages(0)
                .last(true)
                .first(true)
                .empty(true)
                .build();

        when(notificationService.getAllNotifications(any())).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/v1/notifications")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void markAsRead_ShouldReturn200() throws Exception {
        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .status("READ")
                .readTime(LocalDateTime.now())
                .build();

        when(notificationService.markAsRead(1L)).thenReturn(response);

        mockMvc.perform(put("/api/v1/notifications/1/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("READ"));
    }

    @Test
    void cancelNotification_ShouldReturn200() throws Exception {
        NotificationResponse response = NotificationResponse.builder()
                .id(1L)
                .status("FAILED")
                .remarks("Notification cancelled by user")
                .build();

        when(notificationService.cancelNotification(1L)).thenReturn(response);

        mockMvc.perform(put("/api/v1/notifications/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("FAILED"));
    }
}
