package com.spinozanose.springbootrestoo.common;

import java.util.UUID;

/**
 * This class is a representation, albeit simple, of a facade. Creating a unique identifier is
 * an implementation choice, and we delegate that choice to here even if the functionality is in the
 * Java libraries.
 *
 * We could have made this object a first-class domain object, and that would be a fine
 * alternative. It is so simple, though, that leaving the ids as strings in the domain seemed fine.
 * Strings in Java are immutable, just as the UUID must be. If Strings in Java were mutable then I would
 * definitely make this a first-class domain concept.
 */
public class MyUUID {

    private final String uuid;

    public MyUUID() {
        this.uuid = generateNewGuid();
    }

    public String toString() {
        return uuid;
    }

    private static String generateNewGuid() {
        return UUID.randomUUID().toString();
    }
}
