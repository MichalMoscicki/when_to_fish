package com.moscicki.user_service.cron;

import com.moscicki.user_service.manager.SpotNotificationManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationCron {

    @Autowired
    private SpotNotificationManager spotNotificationManager;

    private static final Logger logger = LogManager.getLogger(OrderNotificationCron.class);

    @Scheduled(cron = "0 * * * * *")
    public void orderNotifications() {
        logger.info("ORDERING NOTIFICATION");
        spotNotificationManager.orderNotifications();
    }

}
