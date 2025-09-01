package com.capstone.cargo.controller;

import com.capstone.cargo.dto.CityDTO;
import com.capstone.cargo.model.City;
import com.capstone.cargo.repository.CityRepository;
import com.capstone.cargo.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping()
    public ResponseEntity<List<CityDTO>> getCities(){
        List<CityDTO> getAll = cityService.getAllCities();
        return new ResponseEntity<>(getAll, HttpStatus.OK);
    }
}
