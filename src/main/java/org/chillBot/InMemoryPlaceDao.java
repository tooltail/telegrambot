package org.chillBot;

import java.util.*;

public class InMemoryPlaceDao implements PlaceDao{

    /**
     * In memory db
     */
    private HashSet<Place> places = new LinkedHashSet<>();

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
     * @param str
     * @return
     */
    private String convertToStringWithCapitalLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Adds place to in memory db
     * @param place
     * @return
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
