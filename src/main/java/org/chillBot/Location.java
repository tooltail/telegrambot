package org.chillBot;

import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.dto.Address;

public class Location {

    private Double longitude;
    private Double latitude;

    private static final String API_KEY = "e14bd56f97fa70a13fed7909c72342f9fd4b2d3b";
    private static final String SECRET_KEY = "53783abc30ea25196d5ad04d6125ff9f68f62df3";
    private final DaDataClient dadata = DaDataClientFactory.getInstance(API_KEY, SECRET_KEY);

    public Location(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location() {}

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getDistanceFromLatLonInKm(Location userLocation) {
        Integer R = 6371;
        Double dLat = Math.toRadians(this.latitude- userLocation.latitude);
        Double dLon = Math.toRadians(this.longitude- userLocation.longitude);
        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(Math.toRadians(userLocation.latitude)) * Math.cos(Math.toRadians(this.latitude)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double d = R * c; // Distance in km
        return d;
    }

    public void findPlaceLonLat(Place place) {
        String address = String.format("г Екатеринбург, ул %s", place.getAddress());
        Address addressInfo = dadata.cleanAddress(address);
        this.longitude = addressInfo.getGeoLon();
        this.latitude = addressInfo.getGeoLat();
    }
}
