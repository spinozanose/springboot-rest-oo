package com.spinozanose.springbootrestoo;

import java.util.Map;

public class TestUtils {
    private TestUtils(){}

    public static boolean equals(final Map<String, Object> map1, final Map<String, Object> map2) {
        if (map1.size() != map2.size()) return false;
        for (Map.Entry<String, Object>  map1Entry: map1.entrySet()) {
            if ( ! map2.get(map1Entry.getKey()).equals(map1Entry.getValue())) return false;
        }
        return true;
    }
}
