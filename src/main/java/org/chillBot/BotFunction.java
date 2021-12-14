package org.chillBot;

import org.chillBot.dao.PlaceDao;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Functions of the bot
 */
public class BotFunction implements IBotFunction {

    private PlaceDao placeDao;

    public BotFunction(PlaceDao placeDao) {
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
    public List<String> getPlacesPartly() throws SQLException {
        List<Place> places = placeDao.getPlacesPartly();
        if (places.size() == 0) {
            placeDao.updateStartIdx();
        }
        List<String> formattedOutput = new LinkedList<>();
        for (Place place : places) {
            String result;
            if (place.getRate() == -1) {
                result = String.format("%s (%s) Bar hasn't rated yet",
                        place.getName(),
                        place.getAddress());
            }
            else {
                result = String.format("%s (%s) %s/5",
                        place.getName(),
                        place.getAddress(),
                        String.format(Locale.GERMANY, "%.2f", place.getRate()));
            }
            formattedOutput.add(result);
        }
        return formattedOutput;
    }

    /**
     * Add rate to place
     * @param place
     * @return
     * @throws SQLException
     */
    public boolean addRate(Place place) throws SQLException {
        return placeDao.updateRate(place);
    }
}