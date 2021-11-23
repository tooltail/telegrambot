package org.chillBot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

public interface IBot {
    boolean addPlace(Place place) throws SQLException, TelegramApiException;
    boolean printAllPlaces(String arg) throws SQLException, TelegramApiException;
}
