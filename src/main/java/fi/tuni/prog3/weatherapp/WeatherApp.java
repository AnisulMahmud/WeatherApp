package fi.tuni.prog3.weatherapp;

import  fi.tuni.prog3.weatherapp.Model.WeatherHistoryEntry;
import fi.tuni.prog3.weatherapp.Model.ModelDailyWeatherItem;
import fi.tuni.prog3.weatherapp.Model.ModelHourlyWeatherItem;
import fi.tuni.prog3.weatherapp.Model.FavouriteEntry;
import fi.tuni.prog3.weatherapp.Controller.DailyForecastHandler;
import fi.tuni.prog3.weatherapp.Controller.*;


import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.util.List;
import javafx.scene.control.ListView;
import java.io.*;
import javafx.scene.control.*;


/**
 * JavaFX application class for the WeatherApp.
 * Extends the Application class from JavaFX, providing the entry point for the JavaFX application.
 */
public class WeatherApp extends Application {

    /**
     * API key for accessing the OpenWeatherMap API.
     */
    private static final String API_KEY = "";

    /**
     * The endpoint URL for the OpenWeatherMap forecast API.
     */
    private static final String FORECAST_API_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast";

    /**
     * Common font for the application.
     */
    private static final String FONT_COMMON = "SansSerif";

    /**
     * URL for weather icons from the OpenWeatherMap API.
     */
    private static final String ICON_URL = "http://openweathermap.org/img/wn/";

    /**
     * The root layout of the application.
     */
    private BorderPane root;

    /**
     * Tab for displaying daily weather information.
     */
    private Tab dailyTab;

    /**
     * Tab for displaying hourly weather information.
     */
    private Tab hourlyTab;

    /**
     * Tab for displaying weather history.
     */
    private Tab historyTab;

    /**
     * Tab for displaying favorite weather locations.
     */
    private Tab favouriteTab;

    /**
     * Default city name for weather information.
     */
    public String cityName = "Tampere";

    /**
     * Object for managing weather history.
     */
    private WeatherHistory weatherHistory;

    /**
     * ListView for displaying weather history entries.
     */
    private ListView<String> historyListView;

    /**
     * Object for managing favorite weather locations.
     */
    private FavouriteHistory favouriteHistory;

    /**
     * ListView for displaying favorite weather locations.
     */
    private ListView<String> favouriteListView;

    /**
     * File path for saving weather history data.
     */
    String filePath = "history.json";

    /**
     * File path for saving favorite weather locations data.
     */
    String filePath2 = "favourite.json";

    /**
     * Button for marking a location as a favorite.
     */
    private Button favoriteButton;


    /**
     * The overridden start method from the Application class. Initializes and
     * configures the JavaFX stage, sets up the UI components, and displays the
     * WeatherApp GUI.
     *
     * @param stage The primary stage for the JavaFX application.
     * @throws Exception If an exception occurs during the initialization.
     */
    @Override
    public void start(Stage stage) throws Exception{

        root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        historyListView = new ListView<>();
        weatherHistory = new WeatherHistory(historyListView);
        favouriteListView = new ListView<>();
        favouriteHistory = new FavouriteHistory(favouriteListView);


        this.favoriteButton = new Button("☆");
        this.favoriteButton.setOnAction(event -> toggleFavorites(cityName, this.favoriteButton));


        String fileName = "history.json";
        String fileName2 = "favourite.json";

// Load history from the existing file in resources
        try {
            String filePath = Paths.get(fileName).toAbsolutePath().toString();
        //    System.out.println("filepath from start " + filePath);
            File file = new File(filePath);
            if (file.exists()) {
                // File exists, load history from the file path
                weatherHistory = WeatherHistory.loadHistoryFromFile(historyListView, filePath);
            } else {
                System.err.println("File not found: " + fileName);
                // Handle the error or throw an exception as needed
                return;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }

        // Load favourite from the existing file in resources
        try {
            String filePath2 = Paths.get(fileName2).toAbsolutePath().toString();
        //    System.out.println("filepath from start " + filePath2);
            File file2 = new File(filePath2);
            if (file2.exists()) {
                // File exists, load history from the file path
                favouriteHistory = FavouriteHistory.loadFavouriteHistoryFromFile(favouriteListView, filePath2);
            } else {
                System.err.println("File not found: " + fileName2);
                // Handle the error or throw an exception as needed
                return;
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }


        // VBox centerVBox = getCenterVBox();

        var quitButton = getQuitButton();
        BorderPane.setMargin(quitButton, new Insets(10, 0, 0, 0));
        root.setBottom(quitButton);
        BorderPane.setAlignment(quitButton, Pos.TOP_RIGHT);

        root.setCenter(getCenterVBox());

        //showForecast(root);
        stage.getIcons().add(new Image(WeatherApp.class.getResourceAsStream("/logo.png")));

        Scene scene = new Scene(root, 800, 700);
        stage.setScene(scene);
        stage.setTitle("WeatherApp");
        stage.show();
    }

    /**
     * The main method that launches the WeatherApp application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Creates and returns a VBox that represents the central layout of the WeatherApp.
     * The VBox contains two vertically stacked HBoxes: one for the top section and
     * another for the bottom section.
     *
     * @return The VBox representing the central layout of the WeatherApp.
     */
    private VBox getCenterVBox() {
        VBox centerVBox = new VBox(10);
        centerVBox.setAlignment(Pos.TOP_LEFT);

        // Adding two VBox to the HBox.
        centerVBox.getChildren().addAll(getTopHBox(), getBottomHBox());
        return centerVBox;
    }


    /**
     * Creates and returns a VBox representing the top section of the WeatherApp interface.
     * This section includes a search bar, current weather details, air quality information,
     * sunrise and sunset times, and other relevant weather information.
     *
     * @return The VBox representing the top section of the WeatherApp interface.
     */
    private VBox getTopHBox() {
        //Creating a VBox for the left side.
        VBox leftVBox = new VBox();
        leftVBox.setPrefHeight(400);
        leftVBox.setStyle("-fx-background-image:url('thirdgif.gif');" +
                "-fx-background-size: cover; " +
                "-fx-background-position: center;");


        leftVBox.setAlignment(Pos.CENTER);  // for name of the city
        leftVBox.setPadding(new Insets(30, 30, 30, 30));


        TextField searchBar = new TextField();
        searchBar.setAlignment(Pos.CENTER);
        searchBar.setPromptText("City Name...");

        searchBar.setMinHeight(30);
        searchBar.setMinWidth(250);
        searchBar.setMaxHeight(60);
        searchBar.setMaxWidth(3000);


        Button searchButton = new Button();
        Image searchIcon = new Image(getClass().getResourceAsStream("/search.png"));
        ImageView searchIconView = new ImageView(searchIcon);
        searchIconView.setFitWidth(30);  // Set the width of the icon as needed
        searchIconView.setFitHeight(30);
        searchButton.setGraphic(searchIconView);

        searchButton.setMinHeight(30);
        searchButton.setMinWidth(40);
        searchButton.setMaxHeight(60);
        searchButton.setMaxWidth(100);

        searchButton.setStyle("-fx-background-color: transparent; -fx-border-width: 0;");
        HBox searchItem = new HBox();
        searchItem.setAlignment(Pos.CENTER);
        searchItem.getChildren().addAll(searchBar, searchButton);


        // Label for Date and Time
        Label dateTimeLabel = new Label("Date and Time");
        dateTimeLabel.setAlignment(Pos.TOP_CENTER);
        dateTimeLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 20));
        dateTimeLabel.setPadding(new Insets(20, 0, 20, 0));
        leftVBox.getChildren().add(dateTimeLabel);


        Label cityNameLabel = new Label("City Name");
        cityNameLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 22));
        cityNameLabel.setPadding(new Insets(20, 0, 0, 0));

        Label countryNameLabel = new Label("Country Name");
        countryNameLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 22));
        countryNameLabel.setPadding(new Insets(20, 0, 0, 0));

        this.favoriteButton = new Button(" ☆");

        favoriteButton.setStyle("-fx-background-color: transparent; -fx-border-width: 1;");
        this.favoriteButton.setOnAction(event -> toggleFavorites(cityName, this.favoriteButton));

        HBox cityNameAndFavorite = new HBox();
        cityNameAndFavorite.setAlignment(Pos.CENTER);
        cityNameAndFavorite.getChildren().addAll(cityNameLabel, countryNameLabel, this.favoriteButton);


        ImageView iconImageView = new ImageView();
        iconImageView.setFitWidth(70);
        iconImageView.setFitHeight(70);


        cityNameLabel.setPadding(new Insets(20, 0, 0, 0));

        Label temperatureLabel = new Label("Temperature");
        temperatureLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 40));

        HBox tempDetails = new HBox();
        tempDetails.setPrefHeight(300);
        tempDetails.setAlignment(Pos.CENTER); //for temparature and icon
        tempDetails.setPadding(new Insets(0, 0, 0, 0));

        tempDetails.getChildren().addAll(iconImageView, temperatureLabel);

        Label currentWeatherLabel = new Label("Current Weather");
        currentWeatherLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 15));
        currentWeatherLabel.setPadding(new Insets(10, 0, 10, 0));

        Label feelsLikeLabel = new Label("Feels Like");
        feelsLikeLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
        Image feelsLikeIcon = new Image(getClass().getResourceAsStream("/feels_like.png"));
        ImageView feelsLikeIconView = new ImageView(feelsLikeIcon);
        feelsLikeIconView.setFitWidth(20);
        feelsLikeIconView.setFitHeight(20);
        feelsLikeLabel.setGraphic(feelsLikeIconView);

        //Air Details --------------

        HBox airDetails = new HBox();
        airDetails.setPrefHeight(400);
        airDetails.setAlignment(Pos.CENTER); //for humidity, visibility


//humidity
        Label humidityLabel = new Label("Humidity");
        humidityLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
        humidityLabel.setPadding(new Insets(0, 10, 0, 10));
        Image humidityIcon = new Image(getClass().getResourceAsStream("/humidity.png"));
        ImageView humidityIconView = new ImageView(humidityIcon);
        humidityIconView.setFitWidth(20);
        humidityIconView.setFitHeight(20);
        humidityLabel.setGraphic(humidityIconView);

        /// Air Quality

        Label airQualityLabel = new Label("Air Quality");
        airQualityLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
        airQualityLabel.setPadding(new Insets(0, 10, 0, 10));
        Image airQualityIcon = new Image(getClass().getResourceAsStream("/airQuality.png"));
        ImageView airQualityIconView = new ImageView(airQualityIcon);
        airQualityIconView.setFitWidth(20);
        airQualityIconView.setFitHeight(20);
        airQualityLabel.setGraphic(airQualityIconView);

        //Air speed and icon

        Label airSpeedLabel = new Label("Air Speed");
        airSpeedLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
        airSpeedLabel.setPadding(new Insets(0, 10, 0, 10));
        Image airSpeedIcon = new Image(getClass().getResourceAsStream("/windspeed.png"));
        ImageView airSpeedIconView = new ImageView(airSpeedIcon);
        airSpeedIconView.setFitWidth(20);
        airSpeedIconView.setFitHeight(20);
        airSpeedLabel.setGraphic(airSpeedIconView);

        //Visibility and icon

        Label visibilityLabel = new Label("Visibility");
        visibilityLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
        visibilityLabel.setPadding(new Insets(0, 10, 0, 10));
        Image visibilityIcon = new Image(getClass().getResourceAsStream("/visibility.png"));
        ImageView visibilityIconView = new ImageView(visibilityIcon);
        visibilityIconView.setFitWidth(20);
        visibilityIconView.setFitHeight(20);
        visibilityLabel.setGraphic(visibilityIconView);

        airDetails.getChildren().addAll(humidityLabel, airQualityLabel, airSpeedLabel, visibilityLabel);


        // sunrise -- sunset
        HBox sunDetails = new HBox();
        sunDetails.setPrefHeight(400);
        sunDetails.setAlignment(Pos.CENTER); //for humidity, visibility


        Label sunriseLabel = new Label("Sunrise");
        sunriseLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
        sunriseLabel.setPadding(new Insets(0, 10, 0, 10));
        Image sunriseIcon = new Image(getClass().getResourceAsStream("/sunrise.png"));
        ImageView sunriseIconView = new ImageView(sunriseIcon);
        sunriseIconView.setFitWidth(20);
        sunriseIconView.setFitHeight(20);
        sunriseLabel.setGraphic(sunriseIconView);

        Label sunsetLabel = new Label("Sunset");
        sunsetLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
        sunsetLabel.setPadding(new Insets(0, 10, 0, 10));
        Image sunsetIcon = new Image(getClass().getResourceAsStream("/set2.png"));
        ImageView sunsetIconView = new ImageView(sunsetIcon);
        sunsetIconView.setFitWidth(20);
        sunsetIconView.setFitHeight(20);
        sunsetLabel.setGraphic(sunsetIconView);

        sunDetails.getChildren().addAll(sunriseLabel, sunsetLabel);

        VBox cityContainer = new VBox();
        cityContainer.setAlignment(Pos.CENTER);
        cityContainer.getChildren().addAll(searchItem, cityNameAndFavorite, tempDetails, currentWeatherLabel,
                feelsLikeLabel, airDetails);

        leftVBox.getChildren().add(cityContainer);

        // leftVBox.getChildren().addAll(searchItem, cityNameLabel,favoriteButton, currentWeatherLabel,feelsLikeLabel);
        leftVBox.setMargin(searchButton, new javafx.geometry.Insets(5, 0, 0, 5));

        leftVBox.getChildren().add(tempDetails);

        leftVBox.getChildren().add(airDetails);

        leftVBox.getChildren().add(sunDetails);

        updateWeatherInformation("Tampere", cityNameLabel, countryNameLabel, iconImageView, temperatureLabel,
                currentWeatherLabel, feelsLikeLabel, humidityLabel, airQualityLabel, airSpeedLabel,
                visibilityLabel, dateTimeLabel, sunriseLabel, sunsetLabel);

        searchButton.setOnAction(event -> {
            String cityName1 = searchBar.getText().trim();
            cityName = cityName1;
           // System.out.println("City Name Updated: " + cityName);
            updateCityName(cityName1);
            updateWeatherInformation();
            updateTabsContent();

            if (!cityName.isEmpty()) {
              //  System.out.println(cityName);
                updateWeatherInformation(cityName, cityNameLabel, countryNameLabel, iconImageView, temperatureLabel, currentWeatherLabel,
                        feelsLikeLabel, humidityLabel, airQualityLabel, airSpeedLabel, visibilityLabel, dateTimeLabel, sunriseLabel, sunsetLabel);


            }
        });

        return leftVBox;
    }

    /**
     * Updates the city name used in the WeatherApp. This method is called when the user
     * performs a search for a new city.
     *
     * @param newCityName The new city name to be set in the WeatherApp.
     */
    public void updateCityName(String newCityName) {
        this.cityName = newCityName;

    }


    /**
     * Updates weather information for the current city. This method fetches weather data
     * from the OpenWeatherMap API for the specified city and handles any exceptions that
     * may occur during the process.
     */
    private void updateWeatherInformation() {

        try {

            JsonObject jsonResponse = WeatherApiImpl.fetchWeatherData(cityName);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates and returns an HBox representing the bottom section of the WeatherApp interface.
     * This section includes a TabPane with tabs for daily and hourly forecasts, history, and favorites.
     *
     * @return The HBox representing the bottom section of the WeatherApp interface.
     */
    private HBox getBottomHBox() {
        HBox bottomHBox = new HBox();
        bottomHBox.setPrefHeight(400);
        bottomHBox.setAlignment(Pos.TOP_CENTER);  // for name of the city
        bottomHBox.setPadding(new Insets(30, 5, 0, 5));

        bottomHBox.setStyle("-fx-background-image:url('/4.jpg');" +
                "-fx-background-size: cover; " +
                "-fx-background-position: center;");


        // Create a TabPane to hold daily and hourly forecasts
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Optional: Disable tab closing

        // Create tabs for daily and hourly forecasts
        dailyTab = new Tab("Daily Forecast");
        hourlyTab = new Tab("Hourly Forecast");
        historyTab = new Tab("History");
        favouriteTab = new Tab("Favourite");


        String queryString = "q=" + cityName;
        String completeURL = FORECAST_API_ENDPOINT + "?" + queryString + "&appid=" + API_KEY;
       // System.out.println(completeURL);

        // Create VBox for daily forecast and get the forecast content
        printDailyForecast(completeURL);


        HourlyForecastHandler hourlyForecastHandler = new HourlyForecastHandler(completeURL);
        ListView<ModelHourlyWeatherItem> hourlyWeatherItemListView = hourlyForecastHandler.getHourlyForecastListView();
        hourlyTab.setContent(hourlyWeatherItemListView);


        // handleHistoryTabSelection();

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(dailyTab, hourlyTab, historyTab, favouriteTab);
        bottomHBox.getChildren().addAll(tabPane);

        // Handle the event when the history tab is selected
        historyTab.setOnSelectionChanged(event -> handleHistoryTabSelection());
        favouriteTab.setOnSelectionChanged(event -> handleFavouriteTabSelection());


        return bottomHBox;
    }

    /**
     * Handles the event when the history tab is selected. This method loads and displays
     * historical weather entries from the history file into the history tab's content area.
     */
    private void handleHistoryTabSelection() {
       // System.out.println("Handle history tab selection");

        // Load history entries into the ListView
        try {
            // Specify the correct file path
            String fileName = "history.json";
            String filePath = Paths.get(fileName).toAbsolutePath().toString();
            List<WeatherHistoryEntry> historyEntries = WeatherHistory.loadHistoryFromFile(historyListView, filePath).getHistoryList();

            // Reverse the order of history entries
            Collections.reverse(historyEntries);

            // Create a VBox to hold history entries
            VBox historyContent = new VBox();
            historyContent.setSpacing(10); // Adjust the spacing between entries

            for (WeatherHistoryEntry entry : historyEntries) {

                Label timeLabel = new Label("Time: " + entry.getSearchTime());
                timeLabel.setFont(Font.font(FONT_COMMON, FontWeight.SEMI_BOLD, 16));
                timeLabel.setTextFill(Color.BLACK);

                Label locationLabel = new Label("Location: " + entry.getCityName() + "," + entry.getCountryName());
                locationLabel.setFont(Font.font(FONT_COMMON, FontWeight.SEMI_BOLD, 16));
                locationLabel.setTextFill(Color.BLACK);

                Label temperatureLabel = new Label("Temperature: " + entry.getTemperature() + "°C");
                temperatureLabel.setFont(Font.font(FONT_COMMON, FontWeight.SEMI_BOLD, 16));
                temperatureLabel.setTextFill(Color.BLACK);

                // Add labels to a container VBox for each history entry
                VBox entryContainer = new VBox(locationLabel, timeLabel, temperatureLabel);
                entryContainer.setStyle("-fx-background-color: #1FBED6;-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");


                // Add the entry container to the main content VBox
                historyContent.getChildren().add(entryContainer);
            }

            // Create a ScrollPane to enable scrolling if there are many entries
            ScrollPane scrollPane = new ScrollPane(historyContent);
            scrollPane.setFitToWidth(true); // Allow the ScrollPane to resize horizontally

            // Set the ScrollPane with all history entries as the content
            historyTab.setContent(scrollPane);

        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }


    /**
     * Handles the event when the favorite tab is selected. Loads and displays favorite
     * entries from the favorite file into the favorite tab's content area.
     */
    private void handleFavouriteTabSelection() {
      //  System.out.println("Handle Favourite tab selection");


        // Load history entries into the ListView
        try {
            // Specify the correct file path
            String fileName = "favourite.json";
            String filePath = Paths.get(fileName).toAbsolutePath().toString();
            List<FavouriteEntry> favouriteEntries = FavouriteHistory.loadFavouriteHistoryFromFile(favouriteListView, filePath).getHistoryList();

            // Reverse the order of history entries
            Collections.reverse(favouriteEntries);

            // Create a VBox to hold history entries
            VBox favouriteContent = new VBox();
            favouriteContent.setSpacing(10); // Adjust the spacing between entries

            for (FavouriteEntry entry : favouriteEntries) {

                Label timeLabel = new Label("Current Time: " + formatTimestamp());
                timeLabel.setFont(Font.font(FONT_COMMON, FontWeight.SEMI_BOLD, 16));
                timeLabel.setTextFill(Color.BLACK);

                Label locationLabel = new Label("Location: " + entry.getCityName() + ", " + entry.getCountryName());
                locationLabel.setFont(Font.font(FONT_COMMON, FontWeight.SEMI_BOLD, 16));
                locationLabel.setTextFill(Color.BLACK);


                int currentTemperature = fetchCurrentTemperature(entry.getCityName());
                Label temperatureLabel = new Label("Current Temperature: " + currentTemperature + "°C");
                temperatureLabel.setFont(Font.font(FONT_COMMON, FontWeight.SEMI_BOLD, 16));
                temperatureLabel.setTextFill(Color.BLACK);

                // Add labels to a container VBox for each history entry
                VBox entryContainer = new VBox(locationLabel, timeLabel, temperatureLabel);
                entryContainer.setStyle("-fx-background-color: #1FBED6;-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");


                // Add the entry container to the main content VBox
                favouriteContent.getChildren().add(entryContainer);
            }

            // Create a ScrollPane to enable scrolling if there are many entries
            ScrollPane scrollPane = new ScrollPane(favouriteContent);
            scrollPane.setFitToWidth(true); // Allow the ScrollPane to resize horizontally

            // Set the ScrollPane with all favourite entries as the content
            favouriteTab.setContent(scrollPane);

        } catch (Exception e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
    }

    /**
     * Fetches the current temperature for a given city using the OpenWeatherMap API.
     *
     * @param cityName The name of the city for which the temperature is to be fetched.
     * @return The current temperature in Celsius for the specified city.
     */
    public int fetchCurrentTemperature(String cityName) {
        try {
            JsonObject jsonResponse = WeatherApiImpl.fetchWeatherData(cityName);

            if (jsonResponse != null) {
                double temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
                return (int) (temperature - 273.15); // Convert to Celsius
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0; // Return a default value if temperature fetch fails
    }


    /**
     * Adds the specified city to the list of favorites. Fetches weather data for the city,
     * extracts relevant information, and creates a FavoriteEntry. The entry is then added to
     * the list of favorites, and the updated list is written to the file.
     *
     * @param cityName The name of the city to be added to favorites.
     */
    public void addToFavorites(String cityName) {
        try {
            // Fetch weather data for the city
            JsonObject jsonResponse = WeatherApiImpl.fetchWeatherData(cityName);

            if (jsonResponse != null) {
                if (!favouriteHistory.isLocationInFavorites(cityName)) {
                    // Extract relevant information
                    String city = jsonResponse.get("name").getAsString();
                    String country = jsonResponse.getAsJsonObject("sys").get("country").getAsString();
                    double temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
                    long timestamp = jsonResponse.get("dt").getAsLong();
                    String searchTime = formatTimestamp();
                    int temp = (int) (temperature - 273.15);

                    // Create a FavouriteEntry
                    FavouriteEntry favouriteEntry = new FavouriteEntry(city, country, temp, searchTime);

                    // Add to favourites
                    favouriteHistory.addToFavouriteHistory(favouriteEntry);

                    // Write the updated favourites list to the file
                    favouriteHistory.writeToFile(filePath2);

                    // Update the favourites ListView
                    favouriteHistory.updateFavouriteHistoryListView();

                    System.out.println("Added to favorites: " + city);
                } else {
                    System.out.println("Error: Unable to fetch weather data for adding to favorites.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the content of the tabs in the WeatherApp interface. This method fetches the
     * daily and hourly forecasts for the current city, handles history and favorite tab
     * selections, and updates the corresponding tab contents.
     */
    private void updateTabsContent() {
        String queryString = "q=" + cityName;
        String completeURL = FORECAST_API_ENDPOINT + "?" + queryString + "&appid=" + API_KEY;

        Platform.runLater(() -> {

            // Create VBox for daily forecast and get the forecast content
            printDailyForecast(completeURL);
            handleHistoryTabSelection();
            handleFavouriteTabSelection();


            // Create VBox for hourly forecast and get the forecast content
            HourlyForecastHandler hourlyForecastHandler = new HourlyForecastHandler(completeURL);
            ListView<ModelHourlyWeatherItem> hourlyWeatherItemListView = hourlyForecastHandler.getHourlyForecastListView();
            hourlyTab.setContent(hourlyWeatherItemListView);
        });
    }

    /**
     * Prints the daily forecast for the specified URL. This method uses a DailyForecastHandler
     * to fetch and display daily weather information in a ListView within the daily tab.
     *
     * @param completeURL The complete URL for fetching daily forecast data.
     */
    public void printDailyForecast(String completeURL) {


        DailyForecastHandler dailyForecastHandler = new DailyForecastHandler(completeURL);


        List<ModelDailyWeatherItem> items = new ArrayList<>();
        items = dailyForecastHandler.getDailyForecast();

        ObservableList<ModelDailyWeatherItem> observableItems = FXCollections.observableArrayList(items);

        ListView<ModelDailyWeatherItem> dailyWeatherItemListView = new ListView<>(observableItems);

        //dailyWeatherItemListView.setPrefWidth(100);
        dailyWeatherItemListView.setPrefHeight(500);
        dailyWeatherItemListView.setPrefWidth(400);
        dailyWeatherItemListView.setOrientation(javafx.geometry.Orientation.HORIZONTAL);


        dailyTab.setContent(dailyWeatherItemListView);


        dailyWeatherItemListView.setCellFactory(param -> new ListCell<ModelDailyWeatherItem>() {
            @Override
            protected void updateItem(ModelDailyWeatherItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {

                    VBox graphicContainer = new VBox(10);
                    graphicContainer = new VBox(); // Initialize graphicContainer if not already done
                    graphicContainer.setAlignment(Pos.CENTER);

                    Label dateLabel = new Label(item.getDate());
                    dateLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 20));
                    dateLabel.setTextFill(Color.BLACK);
                    dateLabel.setPadding(new Insets(0, 0, 5, 0)); // Add padding to the bottom for spacing

                    ImageView imageView = dailyForecastHandler.createWeatherIconImageView(item.getWeatherIcon());
                    graphicContainer = new VBox(10, dateLabel, imageView);
                    graphicContainer.setAlignment(Pos.CENTER);

                    setGraphic(graphicContainer);


                    String minTemp = String.format("%.1f", item.getMinTemp());
                    String maxTemp = String.format("%.1f", item.getMaxTemp());
                    int humidity = item.getHumidity();
                    double sealevel = item.getSealevel();


                    Label temperatureLabel = new Label(minTemp + " °C / " + maxTemp + " °C");
                    temperatureLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
                    temperatureLabel.setTextFill(Color.BLACK);
                    temperatureLabel.setPadding(new Insets(0, 10, 0, 10));
                    Image temperatureIcon = new Image(getClass().getResourceAsStream("/temp.png"));
                    ImageView temperatureIconView = new ImageView(temperatureIcon);
                    temperatureIconView.setFitWidth(26);
                    temperatureIconView.setFitHeight(26);
                    temperatureLabel.setGraphic(temperatureIconView);

                    //humidity

                    Label humidityLabel = new Label(humidity + "%");
                    humidityLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
                    humidityLabel.setTextFill(Color.BLACK);
                    humidityLabel.setPadding(new Insets(0, 10, 0, 10));
                    Image humidityIcon = new Image(getClass().getResourceAsStream("/humidity.png"));
                    ImageView humidityIconView = new ImageView(humidityIcon);
                    humidityIconView.setFitWidth(17);
                    humidityIconView.setFitHeight(17);
                    humidityLabel.setGraphic(humidityIconView);

                    //sealevel

                    Label seaLabel = new Label(sealevel + "m");
                    seaLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
                    seaLabel.setTextFill(Color.BLACK);
                    seaLabel.setPadding(new Insets(0, 10, 0, 10));
                    Image seaIcon = new Image(getClass().getResourceAsStream("/sea_level.png"));
                    ImageView seaIconView = new ImageView(seaIcon);
                    seaIconView.setFitWidth(17);
                    seaIconView.setFitHeight(17);
                    seaLabel.setGraphic(seaIconView);


                    setStyle("-fx-background-color: #099898;-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");


                    graphicContainer.getChildren().addAll(temperatureLabel, humidityLabel, seaLabel);
                }
            }
        });


    }

    /**
     * Creates and returns a Quit button. This button can be used to terminate the application
     * when clicked.
     *
     * @return The Quit button.
     */
    private Button getQuitButton() {
        //Creating a button.
        Button button = new Button("Quit");

        //Adding an event to the button to terminate the application.
        button.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        return button;
    }


    /**
     * Updates the weather information for the specified city and sets the corresponding UI elements.
     * This method fetches weather data using the OpenWeatherMap API, extracts relevant information,
     * and updates labels, images, and other UI elements accordingly.
     *
     * @param cityName          The name of the city for which weather information is to be updated.
     * @param cityNameLabel     The label for displaying the city name.
     * @param countryNameLabel  The label for displaying the country name.
     * @param iconImageView     The ImageView for displaying the weather icon.
     * @param temperatureLabel  The label for displaying the temperature.
     * @param currentWeatherLabel The label for displaying the current weather description.
     * @param feelsLikeLabel    The label for displaying the "Feels Like" temperature.
     * @param humidityLabel     The label for displaying humidity information.
     * @param airQualityLabel   The label for displaying air quality information.
     * @param airSpeedLabel     The label for displaying wind speed information.
     * @param visibilityLabel   The label for displaying visibility information.
     * @param dateTimeLabel     The label for displaying date and time.
     * @param sunriseLabel      The label for displaying sunrise time.
     * @param sunsetLabel       The label for displaying sunset time.
     */
    public void updateWeatherInformation(String cityName, Label cityNameLabel, Label countryNameLabel, ImageView iconImageView,
                                          Label temperatureLabel, Label currentWeatherLabel, Label feelsLikeLabel
            , Label humidityLabel, Label airQualityLabel,
                                          Label airSpeedLabel, Label visibilityLabel, Label dateTimeLabel,
                                          Label sunriseLabel, Label sunsetLabel) {

        try {

            JsonObject jsonResponse = WeatherApiImpl.fetchWeatherData(cityName);
          //  System.out.println(jsonResponse);

            if (jsonResponse != null) {

                double lat = jsonResponse.getAsJsonObject("coord").get("lat").getAsDouble();
                double lon = jsonResponse.getAsJsonObject("coord").get("lon").getAsDouble();

                // Extract relevant information
                String city = jsonResponse.get("name").getAsString();
                double temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
                String weatherDescription = jsonResponse.getAsJsonArray("weather")
                        .get(0).getAsJsonObject()
                        .get("description").getAsString();
                double feelsLike = jsonResponse.getAsJsonObject("main").get("feels_like").getAsDouble();
                String weatherIcon = jsonResponse.getAsJsonArray("weather")
                        .get(0).getAsJsonObject()
                        .get("icon").getAsString();
                String windSpeed = jsonResponse.getAsJsonObject("wind").get("speed").getAsString();
                String humidity = jsonResponse.getAsJsonObject("main").get("humidity").toString();
                double visiblity = jsonResponse.get("visibility").getAsDouble();

                long sunrisetime = jsonResponse.getAsJsonObject("sys").get("sunrise").getAsLong();
                long sunsettime = jsonResponse.getAsJsonObject("sys").get("sunset").getAsLong();

                String country = jsonResponse.getAsJsonObject("sys").get("country").getAsString();

                long timestamp = jsonResponse.get("dt").getAsLong();
                String dateTime = formatTimestamp(); // Implement the method to format timestamp
                dateTimeLabel.setText(dateTime);
                cityNameLabel.setText(city);
                countryNameLabel.setText("," + country);
            //    System.out.println("Timestamp from JSON: " + formatTimestamp());
                String iconUrl = ICON_URL + weatherIcon + ".png";
                Image iconImage = new Image(iconUrl);
                iconImageView.setImage(iconImage);

                int tempInt = (int) (temperature - 273.15);
                temperatureLabel.setText(tempInt + "°C");

                currentWeatherLabel.setText(capitalizeEachWord(weatherDescription));

                int feelsLikeInt = (int) (feelsLike - 273.15);
                feelsLikeLabel.setText("Feels Like: " + feelsLikeInt + "°C");


                airSpeedLabel.setText("Wind Speed: " + windSpeed + " m/s");

                humidityLabel.setText("Humidity: " + humidity + "%");

                airQualityLabel.setText("Air Quality: " + airQuality.getAirQuality(lat, lon));

                visibilityLabel.setText("Visibility: " + visiblity / 1000.0 + "km");

                String sunriseTimeValue = formatSunTimestamp(sunrisetime); // Implement the method to format timestamp
                sunriseLabel.setText("Sunrise: " + sunriseTimeValue);

                String sunsetTimeValue = formatSunTimestamp(sunsettime); // Implement the method to format timestamp
                sunsetLabel.setText("Sunset: " + sunsetTimeValue);


                // for history.......................
                WeatherHistoryEntry entry = new WeatherHistoryEntry(cityName, country, tempInt, dateTime);

                if(cityName != "Tampere") {
                    // Add the entry to the history
                    weatherHistory.addToHistory(entry);

                    // Update the history ListView
                    weatherHistory.updateHistoryListView();

                    // Write the updated history list to the file
                    try {
                        weatherHistory.writeToFile("history.json");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                

                boolean isFavorite = favouriteHistory.isLocationInFavorites(cityName);
                updateFavoriteButtonIcon(favoriteButton, isFavorite);
                if (isFavorite) {
                    // Handle the case when the location is a favorite
                    favoriteButton.setText("★");
                    // You might want to update other UI elements related to favorites
                } else {
                    // Handle the case when the location is not a favorite
                    favoriteButton.setText("☆");
                    // You might want to reset or update other UI elements related to favorites
                }


            } else {
                System.out.println("Error: Unable to fetch weather data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Capitalizes the first letter of each word in the given input string.
     *
     * @param input The input string to be capitalized.
     * @return The input string with the first letter of each word capitalized.
     */
    private String capitalizeEachWord(String input) {
        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                c = Character.toTitleCase(c);
                capitalizeNext = false;
            }

            result.append(c);
        }

        return result.toString();
    }


    /**
     * Toggles the favorite status of the specified city and updates the favorite button icon.
     * If the city is already a favorite, it is removed from favorites. Otherwise, it is added
     * to the list of favorites.
     *
     * @param cityName        The name of the city to be toggled.
     * @param favoriteButton  The button used to toggle the favorite status.
     */
    private void toggleFavorites(String cityName, Button favoriteButton) {
        boolean isFavorite = favouriteHistory.isLocationInFavorites(cityName);
        updateFavoriteButtonIcon(favoriteButton, isFavorite);

        if (isFavorite) {

        } else {
            addToFavorites(cityName);

        }
    }


    /**
     * Updates the icon of the favorite button based on the favorite status.
     *
     * @param favoriteButton  The button whose icon is to be updated.
     * @param isFavorite      A boolean indicating whether the city is a favorite or not.
     */
    private void updateFavoriteButtonIcon(Button favoriteButton, boolean isFavorite) {
        String icon = isFavorite ? "★" : "☆";
        favoriteButton.setText(icon);
    }


    /**
     * Formats the current timestamp into a string representation.
     *
     * @return A formatted string representing the current timestamp.
     */
    private String formatTimestamp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mma", Locale.ENGLISH);
        return localDateTime.format(formatter);
    }

    /**
     * Formats a Unix timestamp into a string representation of sunrise or sunset time.
     *
     * @param unixTimestamp The Unix timestamp to be formatted.
     * @return A formatted string representing the sunrise or sunset time.
     */
    private String formatSunTimestamp(long unixTimestamp) {
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" hh : mma ", Locale.ENGLISH);
        return dateTime.format(formatter);
    }

}
