package org.chillBot.dao;

import org.chillBot.Place;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface that define control of the database and local database
 * Interaction with database or local one - data in memory
 */
public interface PlaceDao {
    List<Place> getPlaces(Integer startIdx, Integer endIdx) throws SQLException;
    boolean addPlace(Place place) throws SQLException;
    boolean updateRate(Place place) throws SQLException;
    Integer getNumberOfRows() throws SQLException;
}
