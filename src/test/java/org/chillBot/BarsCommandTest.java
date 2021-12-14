package org.chillBot;

import org.chillBot.dao.InMemoryPlaceDao;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BarsCommandTest {

    /**
     * Checks bot output
     * @throws SQLException
     */
    @Test
    public void testPrintAllPlacesIfBarsExist() throws SQLException {
        Place place1 = new Place("Bar", "Televisor", "Radisheva, 4");
        Place place2 = new Place("Bar", "Melodiya", "Pervomayskaya, 36");
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        BotFunction bot = new BotFunction(placeDao);
        bot.addPlace(place1);
        bot.addPlace(place2);
        List<String> places = bot.getPlacesPartly();
        assertEquals(2, places.size());
        assertEquals("Televisor (Radisheva, 4) Bar hasn't rated yet", places.get(0));
        assertEquals("Melodiya (Pervomayskaya, 36) Bar hasn't rated yet", places.get(1));
    }
}