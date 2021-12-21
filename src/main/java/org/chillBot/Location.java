package org.chillBot;

/**
 * Describes geolocation and operations associated with it
 */
public class Location {

    private Double longitude;
    private Double latitude;

    public Location(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
