package org.chillBot;

import org.chillBot.dao.InMemoryPlaceDao;
import org.junit.Test;

import java.sql.SQLException;
import static org.junit.Assert.assertEquals;

public class BarsCommandTest {

    /**
     * Checks bot output
     * @throws SQLException
     */
    @Test
    public void testPrintAllPlacesIfBarsExist() throws SQLException {
        Place place1 = new Place("Bar", "Televisor", "Radisheva, 4", 1.0);
        Place place2 = new Place("Bar", "Melodiya", "Pervomayskaya, 36", 1.0);
        InMemoryPlaceDao placeDao = new InMemoryPlaceDao();
        Bot bot = new Bot(placeDao);
        bot.addPlace(place1);
        bot.addPlace(place2);
        assertEquals(2, bot.getAllPlaces().size());
        assertEquals("Televisor (Radisheva, 4)", bot.getAllPlaces().get(0));
        assertEquals("Melodiya (Pervomayskaya, 36)", bot.getAllPlaces().get(1));
    }
}