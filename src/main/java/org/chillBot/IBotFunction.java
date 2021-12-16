package org.chillBot;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.List;

public interface IBotFunction {
    boolean addPlace(Place place) throws SQLException, TelegramApiException;
    List<String> getPlaces(Integer startIdx, Integer endIdx) throws SQLException;
    boolean addRate(Place place) throws SQLException, TelegramApiException;
}
