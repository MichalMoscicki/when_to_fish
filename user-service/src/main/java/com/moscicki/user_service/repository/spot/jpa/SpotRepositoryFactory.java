package com.moscicki.user_service.repository.spot.jpa;

import com.moscicki.user_service.entities.spot.LakeSpot;
import com.moscicki.user_service.entities.spot.RiverSpot;
import com.moscicki.user_service.entities.spot.Spot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SpotRepositoryFactory {

    @Autowired
    private RiverSpotRepository riverSpotRepository;
    @Autowired
    private LakeSpotRepository lakeSpotRepository;

    @SuppressWarnings("unchecked")
    public <T extends Spot> BaseSpotRepository<T, String> getRepository(Spot spot) {

        if (spot instanceof RiverSpot) {
            return (BaseSpotRepository<T, String>) riverSpotRepository;
        }
        if (spot instanceof LakeSpot) {
            return (BaseSpotRepository<T, String>) lakeSpotRepository;
        }

        throw new IllegalArgumentException("No repository for class: " + spot.getClass());
    }

    public List<BaseSpotRepository> returnAllRepositories() {
        return Arrays.asList(riverSpotRepository, lakeSpotRepository);
    }

}
