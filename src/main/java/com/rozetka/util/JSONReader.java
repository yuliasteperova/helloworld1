package com.rozetka.util;

import com.rozetka.AbstractTestCase;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSONReader implements ResourceReader {

    protected Map<String, String> values;

    public JSONReader() {
        this.values = readConfig(AbstractTestCase.URL);
    }

    public String get(String fileName, String tagName) throws IOException {
        String value = this.values.get(tagName);
        if (value == null) {
            this.values.putAll(readConfig(fileName));
        }

        return this.values.get(tagName);
    }

    private Map<String, String> readConfig(String fileName) {
        JSONObject jsonObject = null;
        try {
            File file = new File("src/main/resources/json/" + fileName + ".json");
            FileReader reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String eachLine = br.readLine();
            while (eachLine != null) {
                sb.append(eachLine);
                sb.append("\n");
                eachLine = br.readLine();
            }

            jsonObject = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, String> properties = new HashMap<String, String>();

        if (jsonObject != null) {
            Iterator iterator = jsonObject.keys();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();

                try {
                    properties.put(key, jsonObject.getString(key));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return properties;
    }
}