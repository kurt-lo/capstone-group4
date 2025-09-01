package com.capstone.cargo.service;

import com.capstone.cargo.dto.CityDTO;
import com.capstone.cargo.exception.CityMappingException;
import com.capstone.cargo.exception.CityNotFoundException;
import com.capstone.cargo.model.City;
import com.capstone.cargo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<CityDTO> getAllCities() throws CityNotFoundException {
        List<City> cities = cityRepository.findAll();

        if (cities.isEmpty()) {
            throw new CityNotFoundException("No cities found in the database");
        }

        return cities.stream()
                .map(this::mapCityDTO)
                .toList();
    }

    public CityDTO mapCityDTO(City city) throws CityMappingException {
        if (city == null) {
            throw new CityMappingException("City cannot be null");
        }
        if (city.getCountry() == null) {
            throw new CityMappingException("City should have a valid country");
        }
        return new CityDTO(city.getCityId(), city.getCityName(), city.getCountry().getCountryId());
    }
}
