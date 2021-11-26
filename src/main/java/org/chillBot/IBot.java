package org.chillBot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

public interface IBot {
    boolean addPlace(Place place) throws SQLException, TelegramApiException;
    List<String> getAllPlaces() throws SQLException;
}
