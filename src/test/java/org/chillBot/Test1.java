/**
package org.chillBot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Test1 {

    @Mock
    DBPlaceDao dbPlaceDaoMock;

    @Test
    public void testAddSamePlaces() throws TelegramApiException, SQLException {
        assertNotNull(dbPlaceDaoMock);
        Place place = new Place("Bar", "Melodiya", "Pervomayskaya, 36");
        when(dbPlaceDaoMock.addPlace(place)).thenReturn(true).thenReturn(false);
        Bot bot = new Bot();
        assertTrue(bot.addPlace(place));
        assertFalse(bot.addPlace(place));
    }
}
 */