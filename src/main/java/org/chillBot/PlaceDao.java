package org.chillBot;

import java.sql.SQLException;
import java.util.List;

public interface PlaceDao {
    List<Place> getAllPlaces() throws SQLException;
    boolean addPlace(Place place) throws SQLException;
}
