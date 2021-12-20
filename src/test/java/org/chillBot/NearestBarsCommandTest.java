package org.chillBot;

import org.chillBot.dao.InMemoryPlaceDao;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Check /nearestBars command
 */
public class NearestBarsCommandTest {

    /**
     * Checks the output of all bars and order of bars within a radius of 2 km
     * @throws SQLException
     */
    @Test
    public void testIfExistNearestPlaces() throws SQLException {
        List<String> expected = Arrays.asList("The out bar (8 marta, 31)\nrate: 4,00/5\ndistance: 0,24 km",
                "Televisor (Radisheva, 4)\nrate: 3,00/5\ndistance: 0,14 km",
                "Strelka bar (8 marta, 18)\nnot rated\ndistance: 0,22 km");
        BotFunction botFunction = new BotFunction(new InMemoryPlaceDao());
        Place place1 = new Place("Bar", "Televisor", "Radisheva, 4", new Location(60.5997217, 56.830884));
        Place place2 = new Place("Bar", "Strelka Bar", "8 Marta, 18", new Location(60.6000732, 56.8322869));
        Place place3 = new Place("Bar", "The out bar", "8 Marta, 31", new Location(60.6009807, 56.8317491));
        Place place4 = new Place("Bar", "Shelest", "Tkachey, 17", new Location(60.633012, 56.816306));
        botFunction.addPlace(place1);
        botFunction.addPlace(place2);
        botFunction.addPlace(place3);
        botFunction.addPlace(place4);
        place1.setRate(3.0);
        place3.setRate(4.0);
        botFunction.addRate(place1);
        botFunction.addRate(place3);
        Location currentPosition = new Location(60.597460, 56.830860);
        List<String> nearestPlaces = botFunction.getNearestPlaces(currentPosition);
        assertEquals(3, nearestPlaces.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), nearestPlaces.get(i));
        }
    }

    /**
     * Checks if there are no bars within a radius of 2 km
     * @throws SQLException
     */
    @Test
    public void testIfNoNearestPlaces() throws SQLException {
        BotFunction botFunction = new BotFunction(new InMemoryPlaceDao());
        Place place1 = new Place("Bar", "Pan Smetan", "Vostochnaya, 82", new Location(60.6339194, 56.8342123));
        Place place2 = new Place("Bar", "Shelest", "Tkachey, 17", new Location(60.633012, 56.816306));
        Location currentPosition = new Location(60.597460, 56.830860);
        botFunction.addPlace(place1);
        botFunction.addPlace(place2);
        List<String> nearestPlaces = botFunction.getNearestPlaces(currentPosition);
        assertEquals(0, nearestPlaces.size());
    }
}
