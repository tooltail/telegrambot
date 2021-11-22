package org.chillBot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddCommandTest {
    @Mock
    PlaceDao placeDao;

    @Mock
    IBot mbot;

    @InjectMocks
    Bot bot = new Bot();

    @Test
    public void testPlaceAdd() throws SQLException {
        bot.setChatId("1");
        Place place = new Place("Bar", "Televisor", "Radisheva, 4");
        when(bot.addPlace(place)).thenReturn(true);
        assertEquals(bot.addPlace(place), true);
    }
}
