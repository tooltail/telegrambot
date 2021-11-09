package org.chillBot;

import lombok.Getter;
import lombok.Setter;

/**
 * The class that describes the establishment
 */
public class Place {

    /**
     * A field that contains the type of establishment
     */
    @Getter
    @Setter
    private String type;

    /**
     * A field that contains the name of establishment
     */
    @Getter
    @Setter
    private String name;

    /**
     * A field that contains the address of establishment
     */
    @Getter
    @Setter
    private String address;
}
