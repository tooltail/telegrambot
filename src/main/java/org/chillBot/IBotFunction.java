package org.chillBot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

/**
 * Functionality of the bot (methods that need to be implemented)
 */
public interface IBotFunction {

    /**
     * Adds place
     * @param place
     * @return
     * @throws SQLException
     * @throws TelegramApiException
     */
    boolean addPlace(Place place) throws SQLException, TelegramApiException;

    /**
     * Return list of places
     * @param startIdx
     * @param endIdx
     * @return
     * @throws SQLException
     */
    List<String> getPlaces(Integer startIdx, Integer endIdx) throws SQLException;

    /**
     * Adds rate to place
     * @param place
     * @return
     * @throws SQLException
     * @throws TelegramApiException
     */
    boolean addRate(Place place) throws SQLException, TelegramApiException;
}
