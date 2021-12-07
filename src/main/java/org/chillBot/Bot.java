package org.chillBot;

import org.chillBot.dao.PlaceDao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Functions of the bot
 */
public class Bot implements IBot {

    private PlaceDao placeDao;

    public Bot (PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    /**
     * Add place to db
     * @param place added place
     * @return true if added, false if not
     * @throws SQLException
     */
    public boolean addPlace(Place place) throws SQLException {
        return placeDao.addPlace(place);
    }

    /**
     * Gets list of bars
     * @throws SQLException
     */
    public List<String> getAllPlaces() throws SQLException {
        List<Place> places = placeDao.getAllPlaces();
        List<String> formattedOutput = new LinkedList<>();
        for (Place place : places) {
            String result;
            if (place.getRate() == -1) {
                result = String.format("%s (%s) Bar hasn't rated yet",
                        place.getName(),
                        place.getAddress());
            }
            else {
                result = String.format("%s (%s) %.2f/5",
                        place.getName(),
                        place.getAddress(),
                        place.getRate());
            }
            formattedOutput.add(result);
        }
        return formattedOutput;
    }

    public boolean addRate(Place place) throws SQLException {
        return placeDao.updateRate(place);
    }
}