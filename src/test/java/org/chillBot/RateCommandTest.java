package org.chillBot;

import org.chillBot.dao.InMemoryPlaceDao;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

public class RateCommandTest {

    @Test
    public void testIfBarHasNoRating() throws SQLException {
        Place place1 = new Place("Bar", "Televisor", "Radisheva, 4");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        Bot bot = new Bot(placeDao);
        bot.addPlace(place1);
        assertEquals("Televisor (Radisheva, 4) Bar hasn't rated yet", bot.getAllPlaces().get(0));
    }

    @Test
    public void testIfBarHasRating() throws SQLException {
        Place place1 = new Place("Bar", "Televisor", "Radisheva, 4");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        Bot bot = new Bot(placeDao);
        bot.addPlace(place1);
        place1.setRate(4.0);
        bot.addRate(place1);
        place1.setRate(5.0);
        bot.addRate(place1);
        assertEquals("Televisor (Radisheva, 4) 4.50/5", bot.getAllPlaces().get(0));
    }

}
