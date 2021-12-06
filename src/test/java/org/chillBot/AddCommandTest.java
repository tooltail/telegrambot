package org.chillBot;

import org.chillBot.dao.InMemoryPlaceDao;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class AddCommandTest {

    /**
     * Checks if added to db
     * @throws SQLException
     */
    @Test
    public void testAddPlace() throws SQLException {
        Place place = new Place("Bar", "Televisor", "Radisheva, 4");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        Bot bot = new Bot(placeDao);
        boolean result = bot.addPlace(place);
        assertTrue(result);
        assertEquals(1, placeDao.getAllPlaces().size());
        assertEquals(place, placeDao.getAllPlaces().get(0));
    }

    /**
     * Checks if added same places
     * @throws SQLException
     */
    @Test
    public void testAddSamePlaces() throws SQLException {
        Place place = new Place("Bar", "Melodiya", "Pervomayskaya, 36");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        Bot bot = new Bot(placeDao);
        boolean result = bot.addPlace(place);
        assertTrue(result);
        result = bot.addPlace(place);
        assertFalse(result);
    }
}
