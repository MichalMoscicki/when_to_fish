package com.moscicki.user_service.manager;

import com.moscicki.user_service.config.RabbitMQConfig;
import com.moscicki.user_service.dto.notification.OrderNotificationDTO;
import com.moscicki.user_service.dto.spot.SpotType;
import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.repository.SpotNotificationDefinitionRepository;
import com.moscicki.user_service.repository.spot.SpotRepository;
import com.moscicki.user_service.translator.SpotTranslator;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SpotNotificationManager {

    @Autowired
    SpotNotificationDefinitionRepository spotNotificationDefinitionRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public void orderNotifications() {
        int hour = LocalDateTime.now().getHour();
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();

        List<SpotNotificationDefinition> spotNotificationDefinitions = spotNotificationDefinitionRepository.findAllByExecutionHourAndDayOfWeek(hour, dayOfWeek);
        for (SpotNotificationDefinition spotNotificationDefinition : spotNotificationDefinitions) {
            Optional<OrderNotificationDTO> orderNotificationDTOOptional = createPayload(spotNotificationDefinition);
            System.out.println("sending to queue");
            orderNotificationDTOOptional.ifPresent(this::sendToQueue);
            orderNotificationDTOOptional.ifPresent( s -> {
                System.out.println("senfing-to-queue");
            });
            //todo add logging where optional is empty
        }
    }

    private void sendToQueue(OrderNotificationDTO dto) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                dto
        );
    }

    private Optional<OrderNotificationDTO> createPayload(SpotNotificationDefinition def) {
        return spotRepository.findById(def.getSpotId())
                .map(spot -> new OrderNotificationDTO(String.valueOf(def.getType()), spot.getUser().getId(), spot.getId(), String.valueOf(spot.getLon()), String.valueOf(spot.getLat()), spot.getSpotType()));
    }



}
