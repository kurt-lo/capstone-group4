package com.capstone.cargo.service;

import com.capstone.cargo.dto.CityDTO;
import com.capstone.cargo.model.City;
import com.capstone.cargo.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;

    public List<CityDTO> getAllCities(){
        return cityRepository.findAll().stream()
                .map(this::mapCityDTO)
                .toList();
    }

    public CityDTO mapCityDTO(City city){
        return new CityDTO(city.getCityId(), city.getCityName(), city.getCountry().getCountryId());
    }
}
