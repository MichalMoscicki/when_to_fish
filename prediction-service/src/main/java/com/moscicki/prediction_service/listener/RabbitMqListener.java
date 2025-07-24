package com.moscicki.prediction_service.listener;
import com.google.gson.Gson;
import com.moscicki.prediction_service.dto.OrderNotificationDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqListener {

    private static final Gson gson = new Gson();

        @RabbitListener(queues = "order-notification-queue")
        public void receiveMessage(String message) {
            System.out.println("Received: " + message);
            OrderNotificationDTO orderNotificationDTO =  gson.fromJson(message, OrderNotificationDTO.class);
            //translate
            //zapis do jednego obiektu i nadanie statusu new
            //obczajenie czy zapisał się w bazie

    }
}