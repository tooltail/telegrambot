package org.chillBot.dao;

import org.chillBot.Location;
import org.chillBot.Place;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Interaction with database
 */
public class DBPlaceDao implements PlaceDao {

    /**
     * Contains username postgresql database
     */
    private final String user = "postgres";

    /**
     * Contains password postgresql database
     */
    private final String password = "u_8h,B:vV+z[UzK";

    /**
     * Contains table
     */
    private final String tableName = "place";

    /**
     * Gets connection to postgresql database
     * @return connection to postgresql database
     * @throws SQLException
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/telegrambot_schema", user, password);
    }

    /**
     * Gets list of bars from database
     * @return list of bars
     * @throws SQLException
     */
    @Override
    public List<Place> getAllPlaces() throws SQLException {
        String sqlQuery = String.format("SELECT * FROM %s", tableName);
        List<Place> places = new LinkedList<>();
        Statement stmt = getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);
        while (rs.next()) {
            Place place = new Place(rs.getString("type"),
                    rs.getString("name"),
                    rs.getString("address"),
                    (rs.getInt("count") != 0 ? (double)rs.getInt("rate")/rs.getInt("count") : -1),
                    new Location(rs.getDouble("longitude"), rs.getDouble("latitude")));
            places.add(place);
        }
        return places;
    }

    /**
     * Checks place in db
     * @param place place which checks in db
     * @return true if exists in db, false if not
     * @throws SQLException
     */
    private boolean checkPlaceInDB(Place place) throws SQLException {
        List<Place> places = getAllPlaces();
        for (Place pl: places) {
            if (pl.equals(place))
                return true;
        }
        return false;
    }

    /**
     * Converts string to string with capital letter
     * @param str string to be converted
     * @return converted string
     */
    private String convertToStringWithCapitalLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Adds the establishment to the database
     * @param place place which adds
     * @throws SQLException
     */
    @Override
    public boolean addPlace(Place place) throws SQLException {
        place.setType(convertToStringWithCapitalLetter(place.getType()));
        place.setName(convertToStringWithCapitalLetter(place.getName()));
        place.setAddress(convertToStringWithCapitalLetter(place.getAddress()));
        Location placeLocation = new Location();
        placeLocation.findPlaceLonLat(place);
        place.setLocation(placeLocation);
        if (checkPlaceInDB(place))
            return false;
        else {
            String sqlQuery = String.format("INSERT INTO %s (type, name, address, rate, count, longitude, latitude) VALUES('%s', '%s', '%s', '0', '0', '%s', '%s') ON CONFLICT DO NOTHING",
                    tableName, place.getType(), place.getName(), place.getAddress(), place.getLocation().getLongitude(), place.getLocation().getLatitude());
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sqlQuery);
            return true;
        }
    }

    /**
     * Updates rating in database
     * @param place
     * @return update was succesful - True, else - False
     * @throws SQLException
     */
    public boolean updateRate(Place place) throws SQLException {
        if(checkPlaceInDB(place)){
            String sqlQuery = String.format("SELECT count, rate FROM %s WHERE name = '%s' AND address = '%s';", tableName, place.getName(), place.getAddress());
            Statement stmt = getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            rs.next();
            String sqlQuery1 = String.format("UPDATE %s\nSET rate = %s, count = %s\nWHERE type = '%s' AND name = '%s';",
                    tableName, rs.getInt("rate") + place.getRate(), rs.getInt("count") + 1, place.getType(), place.getName());
            stmt.executeUpdate(sqlQuery1);
            return true;
        }
        return false;
    }
}
