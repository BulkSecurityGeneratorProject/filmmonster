package com.example.filmmonster.repository;

import com.example.filmmonster.domain.City;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the City entity.
 */
@SuppressWarnings("unused")
public interface CityRepository extends JpaRepository<City,Long> {

}
