package org.chillBot;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DBPlaceDao implements PlaceDao {

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
     * A method that returns string sql query which insert the establishment to the database
     * @param tableName: table name
     * @param type: type of establishment
     * @param name: name of establishment
     * @param address: address of establishment
     * @return string sql query
    public String returnAddSqlQuery(String tableName, String type, String name, String address) {
        return String.format("INSERT INTO %s (type, name, address) VALUES('%s', '%s', '%s') ON CONFLICT DO NOTHING",
                tableName, type, name, address);
    }
    */

    /**
     * A method that returns string sql query which returns all establishments from databases
     * @param tableName: table name
     * @return string sql query
    public String returnOutputSqlQuery(String tableName) {
        return String.format("SELECT * FROM %s", tableName);
    }
    */

    /**
     * A method that returns connection to postgres database
     * @return connection to postgres database
     */
    public Connection getConnection() throws SQLException {
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

    /**
     * A method that checks whether place was already added to the table or not
     * @param newPlace: new place that would be added by user
     * @return Boolean
    @SneakyThrows
    public Boolean isAddedSamePlace(Place newPlace){
        boolean samePlace = false;
        ResultSet rs = returnListOfBars(returnOutputSqlQuery("place"));
        while (rs.next())
        {
            if(newPlace.getAddress().equals(rs.getString("address"))
                    && newPlace.getName().equals(rs.getString("name"))
                    && newPlace.getType().equals(rs.getString("type")))
            {
                samePlace = true;
                break;
            }
        }
        return samePlace;
    }
    */

    /**
     * A method that writes the establishment to the database
     * @param place
     */
    @Override
    public void addPlace(Place place) throws SQLException {
        String sqlQuery = String.format("INSERT INTO %s (type, name, address) VALUES('%s', '%s', '%s') ON CONFLICT DO NOTHING",
                tableName, place.getType(), place.getName(), place.getAddress());
        Connection con = getConnection();
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sqlQuery);
    }
}
