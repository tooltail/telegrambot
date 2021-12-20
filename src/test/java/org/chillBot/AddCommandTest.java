package org.chillBot;

import org.chillBot.dao.InMemoryPlaceDao;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Check /add command
 */
public class AddCommandTest {

    /**
     * Checks if place was added to db
     */
    @Test
    public void testAddPlace() throws SQLException {
        Place place = new Place("Bar", "Televisor", "Radisheva, 4");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        BotFunction bot = new BotFunction(placeDao);
        boolean result = bot.addPlace(place);
        assertTrue(result);
        assertEquals(1, placeDao.getPlaces(1, 2).size());
        assertEquals(place, placeDao.getPlaces(1, 2).get(0));
    }

    /**
     * Checks if added same places
     */
    @Test
    public void testAddSamePlaces() throws SQLException {
        Place place = new Place("Bar", "Melodiya", "Pervomayskaya, 36");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        BotFunction bot = new BotFunction(placeDao);
        boolean result = bot.addPlace(place);
        assertTrue(result);
        result = bot.addPlace(place);
        assertFalse(result);
        assertEquals(1, placeDao.getPlaces(1, 3).size());
    }
}
