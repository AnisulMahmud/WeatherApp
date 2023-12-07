package fi.tuni.prog3.weatherapp.Controller;

import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Handles reading and writing JSON data to and from files.
 *
 * Implements the {@code iReadAndWriteToFile} interface for standardized file I/O operations.
 */
public class JSONFileIO implements iReadAndWriteToFile {

    /**
     * The JSON data read from or written to a file.
     */
    private JSONObject data;

    /**
     * Reads JSON data from the specified file.
     *
     * @param fileName The name of the file to read JSON data from.
     * @return A JSON-formatted string representing the data read from the file.
     * @throws Exception If an error occurs while reading from the file or parsing JSON.
     */
    @Override
    public String readFromFile(String fileName) throws Exception {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(fileName));
        data = (JSONObject) obj;
        return data.toJSONString();
    }

    /**
     * Writes JSON data to the specified file.
     *
     * @param fileName The name of the file to write JSON data to.
     * @return True if writing to the file is successful, false otherwise.
     * @throws Exception If an error occurs while writing to the file.
     */
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
