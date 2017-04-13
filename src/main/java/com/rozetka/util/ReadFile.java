package com.rozetka.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class ReadFile {
    public String readFileAsString(String filePath) throws IOException {
        /*StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new java.io.FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();*/
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}
