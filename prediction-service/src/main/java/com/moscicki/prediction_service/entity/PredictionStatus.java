package com.moscicki.prediction_service.entity;

public enum PredictionStatus {
    NEW,
    CHECKING_WEATHER,
    CHECKING_WATER_CONDITIONS,
    PREPARING_PREDICTION,
    SEND_ORDER,
    ERROR

}
