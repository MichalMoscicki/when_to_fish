package com.moscicki.user_service.repository.spot;

import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
import com.moscicki.user_service.exception.UserServiceException;
import com.moscicki.user_service.repository.spot.jpa.BaseSpotRepository;
import com.moscicki.user_service.repository.spot.jpa.SpotRepositoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class SpotRepositoryImpl implements SpotRepository {

    @Autowired
    SpotRepositoryFactory spotRepositoryFactory;

    @Override
    public Spot save(Spot spot) {
        BaseSpotRepository<Spot, String> repository = spotRepositoryFactory.getRepository(spot);
        return repository.save(spot);
    }


    @Override
    public void delete(Spot spot) {
        BaseSpotRepository<Spot, String> repository = spotRepositoryFactory.getRepository(spot);
        repository.delete(spot);
    }

    @Override
    public void deleteAll() {
        for (BaseSpotRepository spotRepository : spotRepositoryFactory.returnAllRepositories()) {
            spotRepository.deleteAll();
        }
    }

    @Override
    public Optional<Spot> findBySpotParam(Spot spotParam) {
        if (spotParam.getId() == null) {
            throw new UserServiceException("Spot id can not be null");
        }
        BaseSpotRepository<Spot, String> repository = spotRepositoryFactory.getRepository(spotParam);
        return repository.findById(spotParam.getId());
    }

    @Override
    public List<Spot> findAllByUser(User user) {
        List<Spot> spots = new ArrayList<>();
        for (BaseSpotRepository spotRepository : spotRepositoryFactory.returnAllRepositories()) {
            spots.addAll(spotRepository.findAllByUser(user));
        }
        return spots;
    }

    @Override
    public Optional<Spot> findById(String spotId) {
        for (BaseSpotRepository spotRepository : spotRepositoryFactory.returnAllRepositories()) {
            Optional<Spot> spotOptional = spotRepository.findById(spotId);
            if (spotOptional.isPresent()) {
                return spotOptional;
            }
        }

        return Optional.empty();
    }
}
