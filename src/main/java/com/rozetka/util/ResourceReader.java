package com.rozetka.util;

import java.io.IOException;

public interface ResourceReader {
    String get(String fileName, String tagName) throws IOException;
}
