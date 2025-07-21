package com.moscicki.user_service.cron;

import com.moscicki.user_service.manager.SpotNotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationCron {

    @Autowired
    private SpotNotificationManager spotNotificationManager;

    @Scheduled(cron = "0 * * * *")
    public void orderNotifications() {
        spotNotificationManager.orderNotifications();
    }


}
