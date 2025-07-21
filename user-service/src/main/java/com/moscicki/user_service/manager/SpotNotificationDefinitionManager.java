package com.moscicki.user_service.manager;

import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.exception.UserServiceException;
import com.moscicki.user_service.repository.SpotNotificationDefinitionRepository;
import com.moscicki.user_service.validation.SpotNotificationDefinitionValidator;
import com.moscicki.user_service.validation.SpotValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotNotificationDefinitionManager {

    @Autowired
    SpotNotificationDefinitionRepository spotNotificationDefinitionRepository;


    public SpotNotificationDefinition createSpotNotificationDefinition(Spot spot, SpotNotificationDefinition spotNotificationDefinition) {
        spotNotificationDefinition.setSpotId(spot.getId());
        SpotNotificationDefinitionValidator.validate(spotNotificationDefinition);
        return spotNotificationDefinitionRepository.save(spotNotificationDefinition);
    }

    public List<SpotNotificationDefinition> findAllBySpot(Spot spot) {
        return spotNotificationDefinitionRepository.findAllBySpotId(spot.getId());
    }

    public SpotNotificationDefinition update(Spot spot , SpotNotificationDefinition spotNotificationDefinitionParam) {
        SpotNotificationDefinition spotNotificationDefinition = spotNotificationDefinitionRepository.findById(spotNotificationDefinitionParam.getId()).orElseThrow( () -> new UserServiceException("No spot notification definition with given id: " + spotNotificationDefinitionParam.getId()));
        SpotNotificationDefinitionValidator.validateEdit(spot, spotNotificationDefinition, spotNotificationDefinitionParam);
        merge(spotNotificationDefinition, spotNotificationDefinitionParam);
        return spotNotificationDefinitionRepository.save(spotNotificationDefinition);
    }

    private void merge (SpotNotificationDefinition spotNotificationDefinition, SpotNotificationDefinition spotNotificationDefinitionParam) {
        spotNotificationDefinition.setExecutionHour(spotNotificationDefinitionParam.getExecutionHour());
        spotNotificationDefinition.setDayOfWeek(spotNotificationDefinitionParam.getDayOfWeek());
        spotNotificationDefinition.setType(spotNotificationDefinitionParam.getType());
    }

    public void deleteSpotNotificationDefinitions(Spot spot, List<SpotNotificationDefinition> spotNotificationDefinitions) {
        SpotNotificationDefinitionValidator.validateDelete(spot, spotNotificationDefinitions);
        for (SpotNotificationDefinition spotNotificationDefinition : spotNotificationDefinitions) {
            spotNotificationDefinitionRepository.delete(spotNotificationDefinition);
        }
    }
}
