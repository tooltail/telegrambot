package org.chillBot;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DBPlaceDao implements PlaceDao{

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
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/telegrambot_schema", user, password);
    }

    /**
     * Gets list of bars from database
     * @return list of bars
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
                    rs.getString("address"));
            places.add(place);
        }
        return places;
    }

    /**
     * Checks place in db
     * @param place
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
     * @param str
     * @return
     */
    private String convertToStringWithCapitalLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    /**
     * Adds the establishment to the database
     * @param place
     */
    @Override
    public boolean addPlace(Place place) throws SQLException {
        place.setType(convertToStringWithCapitalLetter(place.getType()));
        place.setName(convertToStringWithCapitalLetter(place.getName()));
        place.setAddress(convertToStringWithCapitalLetter(place.getAddress()));
        if (checkPlaceInDB(place))
            return false;
        else {
            String sqlQuery = String.format("INSERT INTO %s (type, name, address) VALUES('%s', '%s', '%s') ON CONFLICT DO NOTHING",
                    tableName, place.getType(), place.getName(), place.getAddress());
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sqlQuery);
            return true;
        }
    }
}
