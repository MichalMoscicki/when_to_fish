package com.moscicki.user_service.controller;


import com.moscicki.user_service.dto.notification.SpotNotificationDefinitionDTO;
import com.moscicki.user_service.entities.notification.SpotNotificationDefinition;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.manager.SpotManager;
import com.moscicki.user_service.manager.SpotNotificationDefinitionManager;
import com.moscicki.user_service.manager.UserManager;
import com.moscicki.user_service.translator.SpotNotificationDefinitionTranslator;
import com.moscicki.user_service.validation.AccessValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users/{userId}/spots/{spotId}")
@RestController
public class SpotNotificationDefinitionController {

    @Autowired
    UserManager userManager;
    @Autowired
    SpotManager spotManager;
    @Autowired
    SpotNotificationDefinitionManager spotNotificationDefinitionManager;

    @PostMapping
    public ResponseEntity<SpotNotificationDefinitionDTO> createSpotNotificationDefinition(@PathVariable String userId, @PathVariable String spotId, @RequestBody SpotNotificationDefinitionDTO spotNotificationDefinitionDTO) {
        User user = userManager.getUser(userId);
        Spot spot = spotManager.findSpotById(spotId);
        AccessValidator.validate(user, spot);
        SpotNotificationDefinition spotNotificationDefinition =  spotNotificationDefinitionManager.createSpotNotificationDefinition(spot, SpotNotificationDefinitionTranslator.translate(spotNotificationDefinitionDTO));
        return new ResponseEntity<>(SpotNotificationDefinitionTranslator.translate(spotNotificationDefinition), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SpotNotificationDefinitionDTO>> getSpotNotificationDefinitions(@PathVariable String userId, @PathVariable String spotId) {
        User user = userManager.getUser(userId);
        Spot spot = spotManager.findSpotById(spotId);
        AccessValidator.validate(user, spot);
        List<SpotNotificationDefinition> spots = spotNotificationDefinitionManager.findAllBySpot(spot);
        return new ResponseEntity<>(SpotNotificationDefinitionTranslator.translateList(spots), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SpotNotificationDefinitionDTO> updateSpotNotificationDefinition(@PathVariable String userId, @PathVariable String spotId, @RequestBody SpotNotificationDefinitionDTO spotDTO) {
        User user = userManager.getUser(userId);
        Spot spot = spotManager.findSpotById(spotId);
        AccessValidator.validate(user, spot);
        SpotNotificationDefinition spotNotificationDefinition = spotNotificationDefinitionManager.update(spot, SpotNotificationDefinitionTranslator.translate(spotDTO));
        return new ResponseEntity<>(SpotNotificationDefinitionTranslator.translate(spotNotificationDefinition), HttpStatus.OK);
    }


    @DeleteMapping()
    public ResponseEntity<Void> deleteSpotNotificationDefinitions(@PathVariable String userId, @PathVariable String spotId, @RequestBody List<SpotNotificationDefinitionDTO> spots) {
        User user = userManager.getUser(userId);
        Spot spot = spotManager.findSpotById(spotId);
        AccessValidator.validate(user, spot);
        spotNotificationDefinitionManager.deleteSpotNotificationDefinitions(spot, SpotNotificationDefinitionTranslator.translateDTOList(spots));
        return ResponseEntity.ok().build();
    }

}