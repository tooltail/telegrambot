package org.chillBot;


import java.util.Comparator;

/**
 * Class compares places by their rating (from highest to lowest rates)
 */
public class PlaceRateComparator implements Comparator<Place> {

    @Override
    public int compare(Place o1, Place o2) {
        if (o1.getRate() > o2.getRate()) {
            return -1;
        }
        if (o1.getRate() < o2.getRate()) {
            return 1;
        }
        return 0;
    }
}
