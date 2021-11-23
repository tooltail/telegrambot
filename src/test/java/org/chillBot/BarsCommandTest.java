package org.chillBot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BarsCommandTest {

    @Test
    public void testPrintAllPlacesIfNoBars() throws TelegramApiException, SQLException {
        DBPlaceDao dbPlaceDaoMock = Mockito.mock(DBPlaceDao.class);
        when(dbPlaceDaoMock.getAllPlaces()).thenReturn(new LinkedList<>());
        Bot bot = new Bot();
        assertEquals(false, bot.printAllPlaces());
    }

    @Test
    public void testPrintAllPlacesIfBarsExist() throws SQLException, TelegramApiException {
        DBPlaceDao dbPlaceDaoMock = Mockito.mock(DBPlaceDao.class);
        Place place = new Place("Bar", "Televisor", "Radisheva, 4");
        List<Place> list = new LinkedList<>();
        list.add(place);
        when(dbPlaceDaoMock.getAllPlaces()).thenReturn(list);
        Bot bot = new Bot();
        assertEquals(true, bot.printAllPlaces());
    }
}