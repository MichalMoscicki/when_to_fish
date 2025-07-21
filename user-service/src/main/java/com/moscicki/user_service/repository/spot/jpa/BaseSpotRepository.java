package com.moscicki.user_service.repository.spot.jpa;

import com.moscicki.user_service.entities.spot.Spot;
import com.moscicki.user_service.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseSpotRepository<T, ID> extends JpaRepository<T, ID> {
    Optional<T> findBySpotName(String name);

    List<Spot> findAllByUser(User user);

}