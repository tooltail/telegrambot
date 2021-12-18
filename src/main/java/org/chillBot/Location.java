package org.chillBot;

import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.dto.Address;

public class Location {

    private Double longitude;
    private Double latitude;

    private static String API_KEY;
    private static String SECRET_KEY;
    private final DaDataClient client = DaDataClientFactory.getInstance(API_KEY, SECRET_KEY);

    public Location(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Location() {}

    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }

    public static void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

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
        Address addressInfo = client.cleanAddress(address);
        this.longitude = addressInfo.getGeoLon();
        this.latitude = addressInfo.getGeoLat();
    }
}
