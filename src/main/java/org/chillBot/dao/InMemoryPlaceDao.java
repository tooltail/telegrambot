package org.chillBot.dao;

import org.chillBot.Place;
import org.chillBot.dao.PlaceDao;

import java.util.*;

public class InMemoryPlaceDao implements PlaceDao {

    /**
     * In memory db
     */
    private Set<Place> places = new LinkedHashSet<>();

    /**
     * Gets all places
     * @return all places from in memory db
     */
    @Override
    public List<Place> getAllPlaces() {
        return new ArrayList<>(places);
    }

    /**
     * Converts string to string with capital letter
     * @param str string to be converted
     * @return converted string
     */
    private String convertToStringWithCapitalLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Adds the establishment in memory
     * @param place place which adds
     */
    @Override
    public boolean addPlace(Place place) {
        place.setType(convertToStringWithCapitalLetter(place.getType()));
        place.setName(convertToStringWithCapitalLetter(place.getName()));
        place.setAddress(convertToStringWithCapitalLetter(place.getAddress()));
        if (places.contains(place)) {
            return false;
        }
        else {
            places.add(place);
            return true;
        }
    }
}
