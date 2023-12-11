package fi.tuni.prog3.weatherapp.Controller;

import fi.tuni.prog3.weatherapp.Model.WeatherHistoryEntry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents a weather history manager that handles reading and writing weather history entries to a file.
 * Provides methods to add entries, retrieve the history list, read from a file, write to a file, and update a JavaFX ListView.
 */
public class WeatherHistory implements iReadAndWriteToFile {

    /**
     * The list that holds weather history entries.
     */
    private List<WeatherHistoryEntry> historyList;
    /**
     * The JavaFX ListView to be updated with weather history entries.
     */
    private ListView<String> historyListView;


    /**
     * Constructs a WeatherHistory object with the provided ListView for displaying history entries.
     *
     * @param historyListView The ListView to display weather history entries.
     */
    public WeatherHistory(ListView<String> historyListView) {
        this.historyList = new ArrayList<>();
        this.historyListView = historyListView;
    }

    /**
     * Adds a weather history entry to the history list.
     *
     * @param entry The WeatherHistoryEntry to be added.
     */
    public void addToHistory(WeatherHistoryEntry entry) {
        historyList.add(entry);
    }

    /**
     * Gets the list of weather history entries.
     *
     * @return The list of WeatherHistoryEntry objects.
     */
    public List<WeatherHistoryEntry> getHistoryList() {
        return historyList;
    }

    /**
     * Reads the content of a file and returns it as a string.
     *
     * @param fileName The name of the file to read.
     * @return The content of the file as a string.
     * @throws Exception If an error occurs while reading the file.
     */
    @Override
    public String readFromFile(String fileName) throws Exception {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    /**
     * Writes the weather history entries to a file.
     *
     * @param fileName The name of the file to write.
     * @return True if writing to the file is successful, false otherwise.
     * @throws IOException If an error occurs while writing to the file.
     */
    @Override
    public boolean writeToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write("["); // Start of the JSON array
            int size = historyList.size();
            for (int i = 0; i < size; i++) {
                WeatherHistoryEntry entry = historyList.get(i);
                if (entry != null) {
                    String entryJson = gson.toJson(entry);
                    writer.write(entryJson);
                    if (i < size - 1) {
                        writer.write(",");
                    }
                    writer.newLine();
                }
            }
            writer.write("]"); // End of the JSON array
        }
        return true;
    }

    /**
     * Loads weather history entries from a file into the WeatherHistory object.
     *
     * @param historyListView The ListView to display weather history entries.
     * @param filePath        The path of the file to load history entries from.
     * @return A WeatherHistory object with loaded history entries.
     * @throws IOException If an error occurs while reading the file.
     */

    public static WeatherHistory loadHistoryFromFile(ListView<String> historyListView, String filePath) throws IOException {
        WeatherHistory history = new WeatherHistory(historyListView);
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            Gson gson = WeatherHistoryEntry.getCustomGson();
            WeatherHistoryEntry[] entries = gson.fromJson(content, WeatherHistoryEntry[].class);

            if (entries != null) {
                history.getHistoryList().addAll(Arrays.asList(entries));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Updates the ListView with the latest weather history entries.
     */
    public void updateHistoryListView() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (WeatherHistoryEntry entry : historyList) {
            items.add(entry.getCityName() + " - " + entry.getCountryName() + " - " + entry.getSearchTime() + " - " + entry.getTemperature() + "°C");
        }
        historyListView.setItems(items);
    }
}
