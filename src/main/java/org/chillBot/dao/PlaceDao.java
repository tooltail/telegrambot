package org.chillBot.dao;

import org.chillBot.Place;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface that define control of the database and local database
 * Interaction with database or local one - data in memory
 */
public interface PlaceDao {
    /**
     * return List of places stores in database (or memory)
     * @param startIdx
     * @param endIdx
     * @return
     * @throws SQLException
     */
    List<Place> getPlaces(Integer startIdx, Integer endIdx) throws SQLException;

    /**
     * Adds place to database (or memory)
     * @param place
     * @return
     * @throws SQLException
     */
    boolean addPlace(Place place) throws SQLException;

    /**
     * Updates rates of the place in database (or memory)
     * @param place
     * @return
     * @throws SQLException
     */
    boolean updateRate(Place place) throws SQLException;

    /**
     * Gets number of rows in database (or memory)
     * @return
     * @throws SQLException
     */
    Integer getNumberOfRows() throws SQLException;
}
