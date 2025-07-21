package com.moscicki.user_service.manager;

import com.moscicki.user_service.config.RabbitMQConfig;
import com.moscicki.user_service.dto.notification.OrderNotificationDTO;
import com.moscicki.user_service.dto.spot.LakeSpotDTO;
import com.moscicki.user_service.dto.spot.SpotDTO;
import com.moscicki.user_service.entities.notification.NotificationType;
import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.entities.spot.LakeSpot;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.manager.SpotNotificationManager;
import com.moscicki.user_service.repository.SpotNotificationDefinitionRepository;
import com.moscicki.user_service.repository.spot.SpotRepository;
import com.moscicki.user_service.translator.SpotTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SpotNotificationManagerTest {

    //todo ogarnij tetsy !!!!!!

    @Mock
    private SpotNotificationDefinitionRepository spotNotificationDefinitionRepository;

    @Mock
    private SpotRepository spotRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private SpotNotificationManager spotNotificationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOrderNotifications_shouldSendToQueue() {
        // Arrange
        String spotId = UUID.randomUUID().toString();
        int hour = LocalDateTime.now().getHour();
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();
        NotificationType notificationType = NotificationType.EMAIL;

        SpotNotificationDefinition mockDef = new SpotNotificationDefinition();
        mockDef.setSpotId(spotId);
        mockDef.setExecutionHour(hour);
        mockDef.setDayOfWeek(dayOfWeek);
        mockDef.setType(notificationType);

        when(spotNotificationDefinitionRepository.findAllByExecutionHourAndDayOfWeek(hour, dayOfWeek))
                .thenReturn(Collections.singletonList(mockDef));

        Spot spot = new LakeSpot();
        when(spotRepository.findById(spotId)).thenReturn(Optional.of(spot));

        try (MockedStatic<SpotTranslator> translatorMock = Mockito.mockStatic(SpotTranslator.class)) {
            translatorMock.when(() -> SpotTranslator.translate(spot))
                    .thenReturn(new LakeSpotDTO(null, null, null, null, null, null, null));

            // Act
            spotNotificationManager.orderNotifications();

            // Assert
            verify(rabbitTemplate, times(1)).convertAndSend(
                    eq(RabbitMQConfig.EXCHANGE_NAME), // use RabbitMQConfig.EXCHANGE_NAME if accessible
                    eq(RabbitMQConfig.ROUTING_KEY),   // use RabbitMQConfig.ROUTING_KEY
                    any(OrderNotificationDTO.class)
            );
        }
    }

    @Test
    void testOrderNotifications_doesNotSend_whenOptionalEmpty() {
//        // Arrange
//        int hour = LocalDateTime.now().getHour();
//        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();
//
//        SpotNotificationDefinition mockDef = new SpotNotificationDefinition();
//        mockDef.setSpotId(999L);
//        mockDef.setExecutionHour(hour);
//        mockDef.setDayOfWeek(dayOfWeek);
//
//        when(spotNotificationDefinitionRepository.findAllByExecutionHourAndDayOfWeek(hour, dayOfWeek))
//                .thenReturn(Collections.singletonList(mockDef));
//        when(spotRepository.findById(999L)).thenReturn(Optional.empty());
//
//        // Act
//        spotNotificationManager.orderNotifications();
//
//        // Assert
//        verify(rabbitTemplate, never()).convertAndSend(any(), any(), any());
    }
}
