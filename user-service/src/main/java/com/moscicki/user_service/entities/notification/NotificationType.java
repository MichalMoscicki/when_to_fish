package com.moscicki.user_service.entities.notification;

import com.moscicki.user_service.exception.UserServiceException;

public enum NotificationType {
    SMS("SMS"),
    EMAIL("EMAIL");


    private final String value;

    NotificationType(String v) {
        this.value = v;
    }

    public static String getString(NotificationType notificationType) {
        return NotificationType.valueOf(notificationType.toString()).value;
    }


    public static NotificationType getNotificationType(String notificationType) {
        if (notificationType == null) {
            throw new UserServiceException("Notification type can not be null");
        }
        return switch (notificationType.toUpperCase()) {
            case "SMS" -> SMS;
            case "EMAIL" -> EMAIL;
            default -> throw new UserServiceException("No such type: " + notificationType);
        };
    }


}
