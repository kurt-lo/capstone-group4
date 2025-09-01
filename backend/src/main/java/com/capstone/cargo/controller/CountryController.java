package com.capstone.cargo.controller;

import com.capstone.cargo.dto.ContainerDTO;
import com.capstone.cargo.dto.CountryDTO;
import com.capstone.cargo.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping
    public ResponseEntity<List<CountryDTO>> getContainers(){
        List<CountryDTO> getAll = countryService.getAllCountries();
        return new ResponseEntity<>(getAll, HttpStatus.OK);
    }

}
