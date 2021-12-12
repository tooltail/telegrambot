package org.chillBot.dao;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.chillBot.Place;
import java.sql.SQLException;
import java.util.*;

/**
 * Interaction with data in memory
 */
public class InMemoryPlaceDao implements PlaceDao {

    /**
     * In memory db
     */
    private Set<Place> places = new LinkedHashSet<>();

    private Map<Place, Pair<Double, Double>> placePairDictionary = new HashMap<>();
    /**
     * Gets all places
     * @return all places from in memory db
     */
    @Override
    public List<Place> getAllPlaces() {
        ArrayList<Place> arrayList = new ArrayList<>();
        for (Place place : places) {
            if (placePairDictionary.containsKey(place) && placePairDictionary.get(place).getValue() != 0) {
                place.setRate(placePairDictionary.get(place).getKey() / placePairDictionary.get(place).getValue());
            }
            else {
                place.setRate(-1.0);
            }
            arrayList.add(place);
        }
        return arrayList;
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
            Pair<Double, Double> pair = new MutablePair<>(0.0, 0.0);
            placePairDictionary.put(place, pair);
            return true;
        }
    }

    /**
     * Updates rating of the place in memory
     * @param place
     * @return True if rating was added, else False
     * @throws SQLException
     */
    @Override
    public boolean updateRate(Place place) throws SQLException {
        if (places.contains(place)) {
            Pair<Double, Double> oldValue = placePairDictionary.get(place);
            Double rate = oldValue.getKey();
            Double count = oldValue.getValue();
            Pair<Double, Double> pair = new MutablePair<>(rate + place.getRate(), count + 1);
            placePairDictionary.replace(place, oldValue, pair);
            return true;
        }
        return false;
    }
}
