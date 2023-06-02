package com.edts.domain.helper;

import java.util.UUID;

public class IDGenerator {
    public static String generateID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}