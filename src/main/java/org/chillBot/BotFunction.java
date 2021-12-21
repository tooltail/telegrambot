package org.chillBot;

import org.chillBot.dao.PlaceDao;

import java.sql.SQLException;
import java.util.*;

/**
 * Functions of the bot
 */
public class BotFunction implements IBotFunction {

    /**
     * Creates variable for interaction with database
     */
    private PlaceDao placeDao;

    public BotFunction(PlaceDao placeDao) {
        this.placeDao = placeDao;
    }

    /**
     * Add place to db
     * @param place added place
     * @return true if added, false if not
     */
    public boolean addPlace(Place place) {
        try {
            return placeDao.addPlace(place);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets list of bars
     */
    public List<String> getPlaces(Integer startIdx, Integer endIdx) {
        List<Place> places = null;
        try {
            places = placeDao.getPlaces(startIdx, endIdx);
        } catch (SQLException e) {
            e.printStackTrace();
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
     */
    public boolean addRate(Place place) {
        try {
            return placeDao.updateRate(place);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns List of the nearest places in 2 km
     */
    public List<String> getNearestPlaces(Location userLocation) throws SQLException {
        List<Place> places = placeDao.getPlaces(1, placeDao.getNumberOfRows() + 1);
        List<String> formattedOutput = new LinkedList<>();
        Comparator<Place> comparator = new PlaceRateComparator();
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        PriorityQueue<Place> queue = new PriorityQueue<>(10, comparator);
        for (Place place : places) {
            Double distance = distanceCalculator.getDistanceFromLatLonInKm(place.getLocation(), userLocation);
            if (distance <= 2) {
                queue.add(place);
            }
        }
        while (queue.size() != 0) {
            Place place = queue.remove();
            String result;
            Double distance = distanceCalculator.getDistanceFromLatLonInKm(place.getLocation(), userLocation);
            if (place.getRate() == -1) {
                result = String.format("%s (%s)\nnot rated\ndistance: %s km",
                        place.getName(),
                        place.getAddress(),
                        String.format(Locale.GERMANY, "%.2f", distance));
            }
            else {
                result = String.format("%s (%s)\nrate: %s/5\ndistance: %s km",
                        place.getName(),
                        place.getAddress(),
                        String.format(Locale.GERMANY, "%.2f", place.getRate()),
                        String.format(Locale.GERMANY, "%.2f", distance));
            }
            formattedOutput.add(result);
        }
        return formattedOutput;
    }
}