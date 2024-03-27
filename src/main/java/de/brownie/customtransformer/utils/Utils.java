package de.brownie.customtransformer.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class Utils {
    public String getRandomString() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replaceAll("-", "");
        return uuidString.replaceAll("[^a-zA-Z]", "");
    }
}
