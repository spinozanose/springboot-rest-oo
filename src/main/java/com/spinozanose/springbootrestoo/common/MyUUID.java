package com.spinozanose.springbootrestoo.common;

import java.util.UUID;

/**
 * This class is a representation, albeit simple, of a facade. Creating a unique identifier is
 * an implementation choice, and we delegate the decision of what to use to here even though the
 * functionality is in the Java libraries.
 *
 * We could have made this object a first-class domain object, and that would be a fine
 * alternative. It is so simple, though, that leaving the ids as strings in the domain seems fine.
 * It is also likely to be shared, as we are likely to follow the same UUID approach across the
 * application.
 */
public class MyUUID {

    private final String uuid;

    public MyUUID() {
        /*
        Using a method call here is not strictly necessary. We do it this
        way, though, because it gives us the opportunity to describe the
        intention in a way that the Java libraries don't. We want to have
        the term "new" here somewhere to show that we have figured out that
        this mechanism is fine to create a new unique identifier.
         */
        this.uuid = generateNewUuid();
    }

    public String toString() {
        return uuid;
    }

    private static String generateNewUuid() {
        return UUID.randomUUID().toString();
    }
}
