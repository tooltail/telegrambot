/**
package org.chillBot;

import lombok.SneakyThrows;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BarsCommandTest {

    Bot bot;

    @Mock
    DBPlaceDao dbPlaceDao;

    @Test
    public void testPrintAllPlacesIfNoBars() throws TelegramApiException, SQLException {
        when(dbPlaceDao.getAllPlaces()).thenReturn(new LinkedList<>());
        assertEquals(false, bot.printAllPlaces());
    }

    @Test
    public void testPrintAllPlacesIfBarsExist() throws SQLException, TelegramApiException {
        Place place = new Place("Bar", "Televisor", "Radisheva, 4");
        List<Place> list = new LinkedList<>();
        list.add(place);
        when(dbPlaceDao.getAllPlaces()).thenReturn(list);
        assertEquals(true, bot.printAllPlaces());
    }
}
*/