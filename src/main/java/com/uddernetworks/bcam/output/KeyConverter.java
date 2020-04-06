package com.uddernetworks.bcam.output;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyConverter.class);

    private final Map<Integer, List<Integer>> characters = new HashMap<>();

    public KeyConverter() {
        Arrays.asList((int) '\n', (int) '\b', (int) '\t', 0x0C, 0x13, 0x1B, 0x7F, 0x9A, 0x9B, 0x9C).forEach(c -> characters.put(c, Collections.singletonList(c)));

        // Shift is 0x10

        Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
                .forEach(c -> characters.put((int) c, Arrays.asList(0x10, (int) c)));

        Arrays.asList(' ', ',', '-', '.', '/', ';', '=', '[', '\\', ']', '*', '+', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z')
                .forEach(c -> characters.put((int) Character.toLowerCase(c), Collections.singletonList((int) c)));

        this.<Character, Integer>mapOf(
                '"', 0x98,
                '<', 0x99,
                '>', 0xA0,
                '{', 0xA1,
                '}', 0xA2,
                '`', 0xC0,
                '\'', 0xDE,
                '@', 0x40,
                ':', 0x3A,
                '^', 0x5E,
                '$', 0x24,
                '!', 0x21,
                '(', 0x28,
                '#', 0x23,
                '+', 0x2B,
                ')', 0x29,
                '_', 0x5F
                ).forEach((character, key) -> characters.put((int) character, Collections.singletonList(key)));
    }

    public List<Integer> getKeyCombo(int ascii) {
        return characters.getOrDefault(ascii, Collections.singletonList(ascii));
    }

    private <K, V> Map<K, V> mapOf(Object... keyValues) {
        if (keyValues.length % 2 != 0) {
            LOGGER.error("Keys and values are not an even number!");
            return Collections.emptyMap();
        }

        var map = new HashMap<K, V>();
        for (int i = 0; i < keyValues.length; i += 2) {
            map.put((K) keyValues[i], (V) keyValues[i + 1]);
        }

        return map;
    }

}
