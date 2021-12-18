package org.chillBot;

import org.chillBot.dao.InMemoryPlaceDao;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

/**
 * Check /rate command
 */
public class RateCommandTest {
    /**
     * Check whether bot shows right rate (if nothing was added - Bar hasn't rated yet)
     * @throws SQLException
     */
    @Test
    public void testIfBarHasNoRating() throws SQLException {
        Place place1 = new Place("Bar", "Televisor", "Radisheva, 4");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        BotFunction bot = new BotFunction(placeDao);
        bot.addPlace(place1);
        assertEquals("Televisor (Radisheva, 4) Bar hasn't rated yet", bot.getPlaces(1, 2).get(0));
    }

    /**
     * Check logic of rate system (adding to the output and equality)
     * @throws SQLException
     */
    @Test
    public void testIfBarHasRating() throws SQLException {
        Place place1 = new Place("Bar", "Televisor", "Radisheva, 4");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        BotFunction bot = new BotFunction(placeDao);
        bot.addPlace(place1);
        place1.setRate(4.0);
        bot.addRate(place1);
        place1.setRate(5.0);
        bot.addRate(place1);
        assertEquals("Televisor (Radisheva, 4) 4,50/5", bot.getPlaces(1, 2).get(0));
    }
}
