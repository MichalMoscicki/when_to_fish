package com.moscicki.user_service.manager;

import com.moscicki.user_service.entities.spot.LakeSpot;
import com.moscicki.user_service.entities.spot.RiverSpot;
import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.exception.UserServiceException;
import com.moscicki.user_service.repository.spot.SpotRepository;
import com.moscicki.user_service.validation.AccessValidator;
import com.moscicki.user_service.validation.SpotValidator;
import com.moscicki.user_service.validation.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpotManager {

    @Autowired
    SpotRepository spotRepository;


    public Spot createSpot(User user, Spot spot) {
        SpotValidator.validate(spot);
        spot.setUser(user);
        return (Spot) spotRepository.save(spot);
    }

    public List<Spot> findAllByUser(User user) {
        return spotRepository.findAllByUser(user);
    }

    public Spot updateSpot(User user, Spot spotParam) {
        AccessValidator.validate(user, spotParam);
        Spot spot = spotRepository.findBySpotParam(spotParam).orElseThrow( () -> new UserServiceException("No spot with given id: " + spotParam.getId() + " This error can occur if spot type was updated"));
        SpotValidator.validate(spotParam);
        mergeSpots(spot, spotParam);
        return spotRepository.save(spot);
    }

    private void mergeSpots(Spot spot, Spot spotParam) {
        mergeCommonFields(spot, spotParam);
        if (spot instanceof RiverSpot && spotParam instanceof RiverSpot) {
            mergeRiverSpots((RiverSpot) spot, (RiverSpot) spotParam);
        }
        if (spot instanceof LakeSpot && spotParam instanceof LakeSpot) {
            mergeLakeSpots((LakeSpot) spot, (LakeSpot) spotParam);
        }
    }

    private void mergeLakeSpots(LakeSpot spot, LakeSpot spotParam) {
        spot.setLakeName(spotParam.getLakeName());
    }

    private void mergeRiverSpots(RiverSpot spot, RiverSpot spotParam) {
        spot.setRiverName(spotParam.getRiverName());
        spot.setMeasurementStationId(spot.getMeasurementStationId());
        spot.setMinOptimalWaterLevel(spotParam.getMinOptimalWaterLevel());
        spot.setMaxOptimalWaterLevel(spotParam.getMaxOptimalWaterLevel());
    }

    private static void mergeCommonFields(Spot spot, Spot spotParam) {
        spot.setSpotName(spotParam.getSpotName());
        spot.setLat(spotParam.getLat());
        spot.setLon(spotParam.getLon());
    }

    public void deleteSpots(User user, List<Spot> spots) {
        SpotValidator.validateDelete(user, spots);
        for (Spot spot : spots) {
            spotRepository.delete(spot);
        }
    }

    public Spot findSpotById(String spotId) {
        return spotRepository.findById(spotId).orElseThrow( () -> new UserServiceException("No spot with given id: " + spotId));
    }

}
