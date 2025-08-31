package com.capstone.cargo.mapper;

import com.capstone.cargo.model.City;
import com.capstone.cargo.model.Location;

public final class MapperDTOUtils {

    private MapperDTOUtils() {
    }

    public static City cityBuilder(long id) {
        City city = new City();
        city.setCityId(id);

        return city;
    }

    public static Location locationBuilder(long id) {
        Location location = new Location();
        location.setLocationId(id);

        return location;
    }
}
