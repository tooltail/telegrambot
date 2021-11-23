package org.chillBot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class AddCommandTest {
    @Mock
    private DBPlaceDao dbPlaceDaoMock;

    @InjectMocks
    private Bot bot;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddPlace() throws SQLException {
        //PlaceDao dbPlaceDaoMock = Mockito.mock(PlaceDao.class);
        Place place = new Place("Bar", "Televisor", "Radisheva, 4");
        when(dbPlaceDaoMock.addPlace(place)).thenReturn(true);
        //doReturn(true).when(dbPlaceDaoMock).addPlace(place);
        bot = new Bot();
        boolean check = bot.addPlace(place);
        assertEquals(true, check);
    }
}
