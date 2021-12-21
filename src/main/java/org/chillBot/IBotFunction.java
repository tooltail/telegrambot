package org.chillBot;

import java.util.List;

/**
 * Functionality of the bot (methods that need to be implemented)
 */
public interface IBotFunction {

    /**
     * Adds place
     */
    boolean addPlace(Place place);

    /**
     * Return list of places
     */
    List<String> getPlaces(Integer startIdx, Integer endIdx);

    /**
     * Adds rate to place
     */
    boolean addRate(Place place);
}
