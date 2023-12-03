package fi.tuni.prog3.weatherapp;

import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONFileIO implements iReadAndWriteToFile {

    private JSONObject data;

    @Override
    public String readFromFile(String fileName) throws Exception {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(fileName));
        data = (JSONObject) obj;
        return data.toJSONString();
    }

    @Override
    public boolean writeToFile(String fileName) throws Exception {
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            fileWriter.write(data.toJSONString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
            return false;
        }
    }
}
