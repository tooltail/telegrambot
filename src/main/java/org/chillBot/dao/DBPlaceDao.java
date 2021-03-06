package org.chillBot.dao;

import org.chillBot.Location;
import org.chillBot.Place;
import org.chillBot.AddressLonLatFinder;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Interaction with database
 */
public class DBPlaceDao implements PlaceDao {

    /**
     * Contains password postgresql database
     */
    private static String password;

    /**
     * Contains table
     */
    private static String tableName;

    /**
     * Contains username postgresql database
     */
    private static String user;

    /**
     * Contains database's url
     */
    private static String url;

    /**
     * Variable for getting connection
     */
    private static Connection connection;

    public static void setUrl(String url) {
        DBPlaceDao.url = url;
    }

    public static void setUser(String user) {
        DBPlaceDao.user = user;
    }

    public static void setPassword(String password) {
        DBPlaceDao.password = password;
    }

    public static void setTableName(String tableName) {
        DBPlaceDao.tableName = tableName;
    }

    /**
     * Connecting to database
     */
    public DBPlaceDao() {
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets number of rows from database
     */
    public Integer getNumberOfRows() throws SQLException {
        String sqlQuery = String.format("SELECT COUNT(*) FROM %s;", tableName);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);
        rs.next();
        return rs.getInt("count");
    }

    /**
     * get 3 (or another number) bars every time
     */
    @Override
    public List<Place> getPlaces(Integer startIdx, Integer endIdx) throws SQLException {
        String sqlQuery = String.format("SELECT * FROM %s WHERE id >= %s AND id < %s;", tableName, startIdx, endIdx);
        List<Place> places = new LinkedList<>();
        Statement stmt = connection.createStatement();
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
     */
    private boolean checkPlaceInDB(Place place) throws SQLException {
        String sqlQuery = String.format("SELECT * FROM %s WHERE type = '%s' AND name = '%s' AND address = '%s';",
                tableName,
                place.getType(),
                place.getName(),
                place.getAddress());
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sqlQuery);
        if (rs.next()) {
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
     */
    @Override
    public boolean addPlace(Place place) throws SQLException {
        place.setType(convertToStringWithCapitalLetter(place.getType()));
        place.setName(convertToStringWithCapitalLetter(place.getName()));
        place.setAddress(convertToStringWithCapitalLetter(place.getAddress()));
        AddressLonLatFinder lonLatFinder = new AddressLonLatFinder();
        Location placeLocation = lonLatFinder.getAddressLonLat(place.getAddress());
        place.setLocation(placeLocation);
        if (checkPlaceInDB(place))
            return false;
        else {
            String sqlQuery = String.format("INSERT INTO %s (type, name, address, rate, count, longitude, latitude) VALUES('%s', '%s', '%s', '0', '0', '%s', '%s') ON CONFLICT DO NOTHING",
                    tableName, place.getType(), place.getName(), place.getAddress(), place.getLocation().getLongitude(), place.getLocation().getLatitude());
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sqlQuery);
            return true;
        }
    }

    /**
     * Updates rating in database
     * @param place
     * @return update was succesful - True, else - False
     */
    @Override
    public boolean updateRate(Place place) throws SQLException {
        if(checkPlaceInDB(place)){
            String sqlQuery = String.format("SELECT count, rate FROM %s WHERE name = '%s' AND address = '%s';", tableName, place.getName(), place.getAddress());
            Statement stmt = connection.createStatement();
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
