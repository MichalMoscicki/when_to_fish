package com.moscicki.prediction_service.listener;

import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;

@SpringBootTest
public class RabbitMqListenerTest {

    @MockitoBean(answers = Answers.CALLS_REAL_METHODS)
    private RabbitMqListener rabbitMqListener;

    @Test
    void testReceiveMessage() {
        // Given
        String testMessage = "{type: EMAIL, spotDTO:{}}";

        // When
        rabbitMqListener.receiveMessage(testMessage);

        // Then
        verify(rabbitMqListener, times(1)).receiveMessage(testMessage);
    }

}