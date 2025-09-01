package com.capstone.cargo.mapper;

import com.capstone.cargo.model.City;
import com.capstone.cargo.model.Container;

public final class MapperDTOUtils {

    private MapperDTOUtils() {
    }

    public static City cityBuilder(Long id) {
        City city = new City();
        city.setCityId(id);

        return city;
    }

    public static Container containerBuilder(Long id) {
        Container container = new Container();
        container.setContainerId(id);
        return container;
    }
}
