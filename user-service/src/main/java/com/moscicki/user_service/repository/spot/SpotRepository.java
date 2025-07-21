package com.moscicki.user_service.repository.spot;

import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;

import java.util.List;
import java.util.Optional;

public interface SpotRepository {

    Spot save(Spot spot);
    void delete(Spot spot);
    void deleteAll();
    List<Spot> findAllByUser(User user);
    Optional<Spot> findBySpotParam(Spot spot);
    Optional<Spot> findById(String spotId);
}
