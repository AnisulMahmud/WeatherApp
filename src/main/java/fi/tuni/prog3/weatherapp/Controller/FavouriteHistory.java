package fi.tuni.prog3.weatherapp.Controller;
import fi.tuni.prog3.weatherapp.Model.FavouriteEntry;
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
 * Implementation of the {@code iReadAndWriteToFile} interface for file read write.
 * Manages the favorite history, including adding entries, checking if a location is in favorites,
 * reading and writing to a file, loading history from a file, and updating the associated ListView.
 */
public class FavouriteHistory implements iReadAndWriteToFile {

    /**
     * The list of favorite entries.
     */
    private List<FavouriteEntry> favouriteList;

    /**
     * The ListView to display the favorite entries.
     */
    private ListView<String> favouriteListView;

    /**
     * Constructs a new FavouriteHistory with the specified ListView.
     *
     * @param historyListView The ListView to display the favorite entries.
     */
    public FavouriteHistory(ListView<String> historyListView) {
        this.favouriteList = new ArrayList<>();
        this.favouriteListView = historyListView;
    }

    /**
     * Adds a new entry to the favorite history.
     *
     * @param entry The entry to be added.
     */
    public void addToFavouriteHistory(FavouriteEntry entry) {
        favouriteList.add(entry);
    }

    /**
     * Checks if a location is in the list of favorites.
     *
     * @param location The location to check.
     * @return True if the location is in favorites, false otherwise.
     */
    public boolean isLocationInFavorites(String location) {
        return favouriteList.stream().anyMatch(entry -> entry.getCityName().equalsIgnoreCase(location));
    }

    /**
     * Gets the list of favorite entries.
     *
     * @return The list of favorite entries.
     */
    public List<FavouriteEntry> getHistoryList() {
        return favouriteList;
    }


    /**
     * Reads content from a file.
     *
     * @param fileName The name of the file to read.
     * @return The content read from the file.
     * @throws Exception If an error occurs while reading from the file.
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
     * Writes content to a file.
     *
     * @param fileName The name of the file to write to.
     * @return True if writing to the file is successful, false otherwise.
     * @throws IOException If an error occurs while writing to the file.
     */
    @Override
    public boolean writeToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            writer.write("["); // Start of the JSON array
            int size = favouriteList.size();
            for (int i = 0; i < size; i++) {
                FavouriteEntry entry = favouriteList.get(i);
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
     * Loads favorite history from a file and creates a FavouriteHistory instance.
     *
     * @param favouriteListView The ListView to display the favorite entries.
     * @param filePath          The path to the file containing favorite history.
     * @return A FavouriteHistory instance loaded from the file.
     */
    public static FavouriteHistory loadFavouriteHistoryFromFile(ListView<String> favouriteListView, String filePath) {
        FavouriteHistory favourite = new FavouriteHistory(favouriteListView);
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
            Gson gson = FavouriteEntry.getCustomGson();
            FavouriteEntry[] entries = gson.fromJson(content, FavouriteEntry[].class);

            if (entries != null) {
                favourite.getHistoryList().addAll(Arrays.asList(entries));
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
        return favourite;
    }


    /**
     * Updates the associated ListView with the latest favorite entries.
     */
    public void updateFavouriteHistoryListView() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (FavouriteEntry entry : favouriteList) {
            items.add(entry.getCityName() + " - " + entry.getCountryName() + " - " + entry.getSearchTime() + " - " + entry.getTemperature() + "°C");
        }
        favouriteListView.setItems(items);
    }
}
