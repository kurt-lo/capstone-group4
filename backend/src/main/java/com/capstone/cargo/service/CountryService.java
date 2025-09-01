package com.capstone.cargo.service;

import com.capstone.cargo.dto.CountryDTO;
import com.capstone.cargo.model.Country;
import com.capstone.cargo.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryRepository countryRepository;

    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(this::mapCountryToDTO)
                .toList();
    }

    public CountryDTO mapCountryToDTO(Country country) {
        return new CountryDTO(country.getCountryId(), country.getCountryName());
    }
}
