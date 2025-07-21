package com.moscicki.user_service.controller;


import com.moscicki.user_service.dto.spot.SpotDTO;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.manager.SpotManager;
import com.moscicki.user_service.manager.UserManager;
import com.moscicki.user_service.translator.SpotTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users/{userId}/spots")
@RestController
public class SpotController {

    @Autowired
    UserManager userManager;
    @Autowired
    SpotManager spotManager;

    @PostMapping
    public ResponseEntity<SpotDTO> createSpot(@PathVariable String userId, @RequestBody SpotDTO spotDTO) {
        Spot spot = spotManager.createSpot(userManager.getUser(userId), SpotTranslator.translate(spotDTO));
        return new ResponseEntity<>(SpotTranslator.translate(spot), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SpotDTO>> getUserSpots(@PathVariable String userId) {
        List<Spot> spots = spotManager.findAllByUser(userManager.getUser(userId));
        return new ResponseEntity<>(SpotTranslator.translateList(spots), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SpotDTO> updateSpot(@PathVariable String userId, @RequestBody SpotDTO spotDTO) {
        Spot spot = spotManager.updateSpot(userManager.getUser(userId), SpotTranslator.translate(spotDTO));
        return new ResponseEntity<>(SpotTranslator.translate(spot), HttpStatus.OK);
    }


    @DeleteMapping()
    public ResponseEntity<Void> deleteSpots(@PathVariable String userId, @RequestBody List<SpotDTO> spots) {
        spotManager.deleteSpots(userManager.getUser(userId), SpotTranslator.translateDTOList(spots));
        return ResponseEntity.ok().build();
    }

}