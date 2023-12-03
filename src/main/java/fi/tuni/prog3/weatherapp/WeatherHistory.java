package fi.tuni.prog3.weatherapp;

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

public class WeatherHistory implements iReadAndWriteToFile {

    private List<WeatherHistoryEntry> historyList;
    private ListView<String> historyListView;

    public WeatherHistory(ListView<String> historyListView) {
        this.historyList = new ArrayList<>();
        this.historyListView = historyListView;
    }

    public void addToHistory(WeatherHistoryEntry entry) {
        historyList.add(entry);
    }

    public List<WeatherHistoryEntry> getHistoryList() {
        return historyList;
    }

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

    public static WeatherHistory loadHistoryFromFile(ListView<String> historyListView, String filePath) {
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


    public void updateHistoryListView() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (WeatherHistoryEntry entry : historyList) {
            items.add(entry.getCityName() + " - " + entry.getSearchTime() + " - " + entry.getTemperature() + "°C");
        }
        historyListView.setItems(items);
    }
}
