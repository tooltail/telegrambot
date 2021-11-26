package org.chillBot;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * Describes the establishment
 */
public class Place {

    /**
     * Contains the type of establishment
     */
    @Getter
    @Setter
    private String type;

    /**
     * Contains the name of establishment
     */
    @Getter
    @Setter
    private String name;

    /**
     * Contains the address of establishment
     */
    @Getter
    @Setter
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(type, place.type) && Objects.equals(name, place.name) && Objects.equals(address, place.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, address);
    }

    public Place(String type, String name, String address) {
        this.type = type;
        this.name = name;
        this.address = address;
    }

    public Place() {

    }
}
