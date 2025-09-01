package com.capstone.cargo.service;

import com.capstone.cargo.dto.CountryDTO;
import com.capstone.cargo.exception.CountryMappingException;
import com.capstone.cargo.exception.CountryNotFoundException;
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
        List<Country> countries = countryRepository.findAll();

        if (countries.isEmpty()) {
            throw new CountryNotFoundException("No countries found in the database");
        }

        return countries.stream()
                .map(this::mapCountryToDTO)
                .toList();
    }

    public CountryDTO mapCountryToDTO(Country country) {
        if (country == null) {
            throw new CountryMappingException("Country cannot be null");
        }
        return new CountryDTO(country.getCountryId(), country.getCountryName());
    }
}
