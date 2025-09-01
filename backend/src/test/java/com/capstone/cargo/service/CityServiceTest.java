package com.capstone.cargo.service;

import com.capstone.cargo.dto.CityDTO;
import com.capstone.cargo.exception.CityMappingException;
import com.capstone.cargo.exception.CityNotFoundException;
import com.capstone.cargo.model.City;
import com.capstone.cargo.model.Country;
import com.capstone.cargo.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @InjectMocks
    private CityService cityService;

    @Mock
    private CityRepository cityRepository;

    private List<City> cities;

    @BeforeEach
    void setUp() {

        cities = new ArrayList<>();
        cities.add(new City(1, "Manila", new Country(1, "Philippines")));
        cities.add(new City(2, "Cebu", new Country(1, "Philippines")));
        cities.add(new City(3, "Seoul", new Country(2, "Korea")));
        cities.add(new City(4, "Tokyo", new Country(3, "Japan")));
        cities.add(new City(5, "Busan", new Country(2, "Korea")));

    }

    @Test
    void test_givenCities_whenGetAllCities_thenReturnCityDTOList() throws CityMappingException {
        when(cityRepository.findAll()).thenReturn(cities);

        List<CityDTO> result = cityService.getAllCities();

        assertNotNull(result);
        assertEquals(5, result.size());
        assertEquals("Manila", result.get(0).getCityName());

        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void test_givenNoCities_whenGetAllCities_thenThrowCityNotFoundException() {
        when(cityRepository.findAll()).thenReturn(new ArrayList<>());

        CityNotFoundException exception = assertThrows(CityNotFoundException.class, () -> {
            cityService.getAllCities();
        });

        assertEquals("No cities found in the database", exception.getMessage());

        verify(cityRepository, times(1)).findAll();
    }

    @Test
    void test_givenNullCity_whenMapCityDTO_thenThrowCityMappingException() {
        CityMappingException exception = assertThrows(CityMappingException.class, () -> {
            cityService.mapCityDTO(null);
        });

        assertEquals("City cannot be null", exception.getMessage());
    }

    @Test
    void test_givenCityWithNullCountry_whenMapCityDTO_thenThrowCityMappingException() {
        City cityWithNullCountry = new City(6, "Unknown", null);

        CityMappingException exception = assertThrows(CityMappingException.class, () -> {
            cityService.mapCityDTO(cityWithNullCountry);
        });

        assertEquals("City should have a valid country", exception.getMessage());
    }

    @Test
    void test_givenValidCity_whenMapCityDTO_thenReturnCityDTO() throws CityMappingException {
        CityDTO cityDTO = cityService.mapCityDTO(cities.get(0));

        assertNotNull(cityDTO);
        assertEquals(1, cityDTO.getId());
        assertEquals("Manila", cityDTO.getCityName());
        assertEquals(1, cityDTO.getCountry());
    }


}