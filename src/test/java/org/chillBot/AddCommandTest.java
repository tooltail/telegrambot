package org.chillBot;

import org.junit.Test;
import org.mockito.Mockito;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class AddCommandTest {

    @Test
    public void testAddPlace() throws SQLException {
        DBPlaceDao dbPlaceDaoMock = Mockito.mock(DBPlaceDao.class);
        Place place = new Place("Bar", "Televisor", "Radisheva, 4");
        when(dbPlaceDaoMock.addPlace(place)).thenReturn(true);
        Bot bot = new Bot();
        bot.dbDao = dbPlaceDaoMock;
        boolean check = bot.addPlace(place);
        assertEquals(true, check);
    }

    @Test
    public void testAddSamePlaces() throws TelegramApiException, SQLException {
        DBPlaceDao dbPlaceDaoMock = Mockito.mock(DBPlaceDao.class);
        Place place = new Place("Bar", "Melodiya", "Pervomayskaya, 36");
        when(dbPlaceDaoMock.addPlace(place)).thenReturn(true).thenReturn(false);
        Bot bot = new Bot();
        bot.dbDao = dbPlaceDaoMock;
        assertTrue(bot.addPlace(place));
        assertFalse(bot.addPlace(place));
    }
}
