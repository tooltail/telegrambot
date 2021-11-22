package org.chillBot;

import java.sql.SQLException;
import java.util.List;

public interface PlaceDao {
    public List<Place> getAllPlaces() throws SQLException;
    public void addPlace(Place place) throws SQLException;
}
