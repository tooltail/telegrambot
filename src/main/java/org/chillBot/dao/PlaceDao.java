package org.chillBot.dao;

import org.chillBot.Place;

import java.sql.SQLException;
import java.util.List;

public interface PlaceDao {
    void updateStartIdx();
    List<Place> getPlacesPartly() throws SQLException;
    List<Place> getAllPlaces() throws SQLException;
    boolean addPlace(Place place) throws SQLException;
    boolean updateRate(Place place) throws SQLException;
}
