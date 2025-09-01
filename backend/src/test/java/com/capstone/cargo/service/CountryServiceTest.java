package com.capstone.cargo.service;

import com.capstone.cargo.dto.CountryDTO;
import com.capstone.cargo.exception.CountryMappingException;
import com.capstone.cargo.exception.CountryNotFoundException;
import com.capstone.cargo.model.Country;
import com.capstone.cargo.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @InjectMocks
    private CountryService countryService;

    @Mock
    private CountryRepository countryRepository;

    private List<Country> countries;

    @BeforeEach
    void setUp() {
        countries = new ArrayList<>();
        countries.add(new Country(1L, "Philippines"));
        countries.add(new Country(2L, "Japan"));
        countries.add(new Country(3L, "Korea"));
    }

    @Test
    void test_getAllCountries_whenCountriesExist_thenReturnCountryDTOList() {
        when(countryRepository.findAll()).thenReturn(countries);

        List<CountryDTO> allCountries = countryService.getAllCountries();

        assertNotNull(allCountries);
        assertEquals(3, allCountries.size());
        assertEquals("Philippines", allCountries.get(0).getCountryName());
        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void test_getAllCountries_whenNoCountries_thenThrowException() {
        when(countryRepository.findAll()).thenReturn(Collections.emptyList());

        CountryNotFoundException exception = assertThrows(CountryNotFoundException.class, () -> countryService.getAllCountries());
        assertEquals("No countries found in the database", exception.getMessage());

        verify(countryRepository, times(1)).findAll();
    }

    @Test
    void test_getAllCountries_whenCountryIsNull_thenThrowException() {
        CountryMappingException exception = assertThrows(CountryMappingException.class, () -> {
            countryService.mapCountryToDTO(null);
        });

        assertEquals("Country cannot be null", exception.getMessage());
    }

    @Test
    void test_mapCountryToDTO_whenValidCountry_thenReturnDTO() {
        Country country = new Country(10L, "Singapore");

        CountryDTO countryDTO = countryService.mapCountryToDTO(country);

        assertNotNull(countryDTO);
        assertEquals(10L, countryDTO.getCountryId());
        assertEquals("Singapore", countryDTO.getCountryName());
    }
}