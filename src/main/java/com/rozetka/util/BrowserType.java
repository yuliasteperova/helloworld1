package com.rozetka.util;

public enum BrowserType {
    FIREFOX,
    CHROME,
    PHANTOM;

    public static BrowserType parse(String type) {
        for (BrowserType browserType : BrowserType.values()) {
            if (browserType.name().equalsIgnoreCase(type)) {
                return browserType;
            }
        }
        return null;
    }
}
