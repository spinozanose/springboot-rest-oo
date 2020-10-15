package com.spinozanose.springbootrestoo.implementation;

import java.util.UUID;

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
