package com.moscicki.user_service.repository;

import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpotNotificationDefinitionRepository extends JpaRepository<SpotNotificationDefinition, String> {


    List<SpotNotificationDefinition> findAllBySpotId(String id);

    List<SpotNotificationDefinition> findAllByExecutionHourAndDayOfWeek(int hour, int dayOfWeek);

}
