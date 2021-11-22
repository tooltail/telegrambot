package org.chillBot;

import java.sql.SQLException;

public interface IBot {
    public boolean addPlace(Place place) throws SQLException;
    public void printAllPlaces();
}
