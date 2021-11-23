package org.chillBot;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBPlaceDao implements PlaceDao{

    /**
     * A field that contains username postgres database
     */
    private final String user = "postgres";

    /**
     * A field that contains password postgres database
     */
    private final String password = "u_8h,B:vV+z[UzK";

    private final String tableName = "place";

    /**
     * A method that returns connection to postgres database
     * @return connection to postgres database
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/telegrambot_schema", user, password);
    }

    /**
     * A method that returns list of bars from database
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

    private boolean existPlaceInDB(Place place) throws SQLException {
        List<Place> places = getAllPlaces();
        for (Place pl: places) {
            if (pl.equals(place))
                return true;
        }
        return false;
    }

    /**
     * A method that writes the establishment to the database
     * @param place
     */
    @Override
    public boolean addPlace(Place place) throws SQLException {
        if (existPlaceInDB(place))
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
