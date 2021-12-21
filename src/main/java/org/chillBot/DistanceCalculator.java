package org.chillBot;

/**
 * Class for calculating distance between two coordinates
 */
public class DistanceCalculator {
    /**
     * Counts distance between user and place in kilometres
     */
    public Double getDistanceFromLatLonInKm(Location placeLocation, Location userLocation) {
        Integer radiusOfTheEarth = 6371;
        Double deltaLat = Math.toRadians(placeLocation.getLatitude() - userLocation.getLatitude());
        Double deltaLon = Math.toRadians(placeLocation.getLongitude() - userLocation.getLongitude());
        Double temp = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                Math.cos(Math.toRadians(userLocation.getLatitude())) * Math.cos(Math.toRadians(placeLocation.getLatitude())) * Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        Double distanceInKm = radiusOfTheEarth * 2 * Math.atan2(Math.sqrt(temp), Math.sqrt(1-temp));
        return distanceInKm;
    }
}
