package org.chillBot;

import java.util.Objects;

/**
 * Describes the establishment
 */
public class Place {

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getRate() { return rate; }

    public void setRate(Double rate) { this.rate = rate; }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Contains the type of establishment
     */
    private String type;

    /**
     * Contains the name of establishment
     */
    private String name;

    /**
     * Contains the address of establishment
     */
    private String address;

    /**
     * Average rate
     */
    private Double rate;

    private Location location;

    /**
     * Override method for comparing places using their name, address and type
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(type, place.type) && Objects.equals(name, place.name) && Objects.equals(address, place.address);
    }

    /**
     * Generates a hash code for variables
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, name, address);
    }

    public Place(String type, String name, String address) {
        this.type = type;
        this.name = name;
        this.address = address;
    }

    public Place(String type, String name, String address, Location location) {
        this.type = type;
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public Place(String type, String name, String address, Double rate, Location location) {
        this.type = type;
        this.name = name;
        this.address = address;
        this.rate = rate;
        this.location = location;
    }

    public Place() { }
}
