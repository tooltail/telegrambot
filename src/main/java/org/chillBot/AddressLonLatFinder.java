package org.chillBot;

import ru.redcom.lib.integration.api.client.dadata.DaDataClient;
import ru.redcom.lib.integration.api.client.dadata.DaDataClientFactory;
import ru.redcom.lib.integration.api.client.dadata.dto.Address;

/**
 * Class for finding address longitude and latitude
 */
public class AddressLonLatFinder {

    private static String API_KEY;
    private static String SECRET_KEY;
    private static DaDataClient client;

    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }

    public static void setSecretKey(String secretKey) {
        SECRET_KEY = secretKey;
    }

    public static void setDataClient() {
        client = DaDataClientFactory.getInstance(API_KEY, SECRET_KEY);
    }

    /**
     * Finds address longitude and latitude
     */
    public Location getAddressLonLat(String address) {
        String query = String.format("г Екатеринбург, ул %s", address);
        Address addressInfo = client.cleanAddress(query);
        return new Location(addressInfo.getGeoLon(), addressInfo.getGeoLat());
    }
}
