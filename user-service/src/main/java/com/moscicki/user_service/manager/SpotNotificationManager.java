package com.moscicki.user_service.manager;

import com.google.gson.Gson;
import com.moscicki.user_service.config.RabbitMQConfig;
import com.moscicki.user_service.dto.notification.OrderNotificationDTO;
import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.repository.SpotNotificationDefinitionRepository;
import com.moscicki.user_service.repository.spot.SpotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SpotNotificationManager {

    private static final Logger logger = LoggerFactory.getLogger(SpotNotificationManager.class);

    @Autowired
    SpotNotificationDefinitionRepository spotNotificationDefinitionRepository;
    @Autowired
    SpotRepository spotRepository;
    @Autowired
    RabbitTemplate rabbitTemplate;

    private Gson gson = new Gson();

    public void orderNotifications() {
        int hour = LocalDateTime.now().getHour();
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();


        List<SpotNotificationDefinition> spotNotificationDefinitions = spotNotificationDefinitionRepository.findAllByExecutionHourAndDayOfWeek(hour, dayOfWeek);
        logger.info("spotNotificationDefinitions.size(): {}", spotNotificationDefinitions.size());
        for (SpotNotificationDefinition spotNotificationDefinition : spotNotificationDefinitions) {
            Optional<OrderNotificationDTO> orderNotificationDTOOptional = createPayload(spotNotificationDefinition);
            logger.info("Try to send to queue");
            orderNotificationDTOOptional.ifPresent(this::sendToQueue);
            orderNotificationDTOOptional.ifPresent( s -> {
                System.out.println("sending-to-queue");
            });
            //todo add logging where optional is empty
            //todo add last generationDate
        }
    }

    private void sendToQueue(OrderNotificationDTO dto) {
        logger.info("Sending to queue. {}", dto.type());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                gson.toJson(dto)
        );
    }

    private Optional<OrderNotificationDTO> createPayload(SpotNotificationDefinition def) {
        return spotRepository.findById(def.getSpotId())
                .map(spot -> new OrderNotificationDTO(String.valueOf(def.getType()), spot.getUser().getId(), spot.getId(), String.valueOf(spot.getLon()), String.valueOf(spot.getLat()), spot.getSpotType()));
    }



}
