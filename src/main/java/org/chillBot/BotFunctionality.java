package org.chillBot;

import org.chillBot.dao.PlaceDao;

import java.sql.SQLException;
import java.util.*;

/**
 * Functions of the bot
 */
public class BotFunctionality implements IBot {

    private PlaceDao placeDao;

    public BotFunctionality(PlaceDao placeDao) {
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
                result = String.format("%s (%s) %s/5",
                        place.getName(),
                        place.getAddress(),
                        String.format(Locale.GERMANY, "%.2f", place.getRate()));
            }
            formattedOutput.add(result);
        }
        return formattedOutput;
    }

    public boolean addRate(Place place) throws SQLException {
        return placeDao.updateRate(place);
    }



    public List<String> getNearestPlace(Location userLocation) throws SQLException {
        List<Place> places = placeDao.getAllPlaces();
        List<String> formattedOutput = new LinkedList<>();
        Comparator<Place> comparator = new PlaceRateComparator();
        PriorityQueue<Place> queue = new PriorityQueue<>(10, comparator);
        for (Place place : places) {
            Location location = place.getLocation();
            Double distance = location.getDistanceFromLatLonInKm(userLocation);
            if (distance <= 2 && place.getRate() != -1) {
                queue.add(place);
            }
        }
        while (queue.size() != 0) {
            Place place = queue.remove();
            String result = String.format("%s (%s)\nrate: %s/5\ndistance: %s km",
                    place.getName(),
                    place.getAddress(),
                    String.format(Locale.GERMANY, "%.2f", place.getRate()),
                    String.format(Locale.GERMANY, "%.2f", place.getLocation().getDistanceFromLatLonInKm(userLocation)));
            formattedOutput.add(result);
        }
        return formattedOutput;
    }
}