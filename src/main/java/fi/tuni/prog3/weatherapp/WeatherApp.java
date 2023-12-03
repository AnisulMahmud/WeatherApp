package fi.tuni.prog3.weatherapp;

import  fi.tuni.prog3.weatherapp.Model.WeatherHistoryEntry;
import fi.tuni.prog3.weatherapp.Model.ModelDailyWeatherItem;
import fi.tuni.prog3.weatherapp.Model.ModelHourlyWeatherItem;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.io.*;



import javafx.scene.control.*;



public class WeatherApp extends Application {

    private static final String API_KEY ="23a54643d49faf711fbbd48521054055";
    private static final String FORECAST_API_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast";



    //private static final String MAP_API_ENDPOINT = "https://tile.openweathermap.org/map/{layer}/{z}/{x}/{y}.png?appid={API key}";
    private static final String FONT_COMMON = "SansSerif";
    private static final String ICON_URL = "http://openweathermap.org/img/wn/";

    private BorderPane root;
    private WebView mapView;
    private WebEngine mapWebEngine;
    private Tab dailyTab;
    private Tab hourlyTab;
    private Tab mapTab;
    private Tab historyTab;


   public String cityName= "Tampere";

    private WeatherHistory weatherHistory;
    private ListView<String> historyListView;
    String filePath = "history.json";



    @Override
    public void start(Stage stage) {

        root = new BorderPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        historyListView = new ListView<>();
        weatherHistory = new WeatherHistory(historyListView);


        String fileName = "history.json";

// Load history from the existing file in resources
        try {
            String filePath = Paths.get(fileName).toAbsolutePath().toString();
            System.out.println("filepath from start "+filePath);
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

    public static void main(String[] args) {
        launch();
    }

    private VBox getCenterVBox() {
        VBox centerVBox = new VBox(10);
        centerVBox.setAlignment(Pos.TOP_LEFT);

        // Adding two VBox to the HBox.
        centerVBox.getChildren().addAll(getTopHBox(), getBottomHBox());
        return centerVBox;
    }


    
    private VBox getTopHBox() {
        //Creating a VBox for the left side.
        VBox leftVBox = new VBox();
        leftVBox.setPrefHeight(400);
        leftVBox.setStyle("-fx-background-image:url('/1.jpg');" +
                "-fx-background-size: cover; " +
                        "-fx-background-position: center;");


        leftVBox.setAlignment(Pos.CENTER);  // for name of the city
        leftVBox.setPadding(new Insets(30,30,30,30));


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
        searchItem.getChildren().addAll(searchBar,searchButton);



        // Label for Date and Time
        Label dateTimeLabel = new Label("Date and Time");
        dateTimeLabel.setAlignment(Pos.TOP_CENTER);
        dateTimeLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 20));
        dateTimeLabel.setPadding(new Insets(20,0,20,0));
        leftVBox.getChildren().add(dateTimeLabel);

        Label cityNameLabel = new Label("City Name");
        cityNameLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 20));
        cityNameLabel.setPadding(new Insets(20,0,0,0));
        
        ImageView iconImageView = new ImageView();
        iconImageView.setFitWidth(70);
        iconImageView.setFitHeight(70);
        
        cityNameLabel.setPadding(new Insets(20,0,0,0));
                
        Label temperatureLabel = new Label("Temperature");
        temperatureLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 40));
        
        HBox tempDetails = new HBox();
        tempDetails.setPrefHeight(300);
        tempDetails.setAlignment(Pos.CENTER); //for temparature and icon
        tempDetails.setPadding(new Insets(0,0,0,0));
        
        tempDetails.getChildren().addAll(iconImageView, temperatureLabel);
        
        Label currentWeatherLabel = new Label("Current Weather");
        currentWeatherLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD,15));
        currentWeatherLabel.setPadding(new Insets(10,0,10,0));
        
        Label feelsLikeLabel = new Label("Feels Like");
        feelsLikeLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD,12));
        Image feelsLikeIcon = new Image(getClass().getResourceAsStream("/feels_like.png"));
        ImageView feelsLikeIconView = new ImageView(feelsLikeIcon) ;
        feelsLikeIconView .setFitWidth(20);
        feelsLikeIconView .setFitHeight(20);
        feelsLikeLabel.setGraphic(feelsLikeIconView );

        HBox airDetails = new HBox();
        airDetails.setPrefHeight(400);
        airDetails.setAlignment(Pos.CENTER); //for humidity, visibility

        // Add daily forecast


      //humidity Label and icon
        
        Label humidityLabel = new Label("Humidity");
        humidityLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD,12));
        humidityLabel.setPadding(new Insets(0,10,0,10));
        Image humidityIcon = new Image(getClass().getResourceAsStream("/humidity.png"));
        ImageView humidityIconView = new ImageView(humidityIcon);
        humidityIconView.setFitWidth(20);
        humidityIconView.setFitHeight(20);
        humidityLabel.setGraphic(humidityIconView);

    //Air speed and icon
        
        Label airSpeedLabel = new Label("Air Speed");
        airSpeedLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD,12));
        airSpeedLabel.setPadding(new Insets(0,10,0,10));
        Image airSpeedIcon = new Image(getClass().getResourceAsStream("/windspeed.png"));
        ImageView airSpeedIconView = new ImageView(airSpeedIcon);
        airSpeedIconView.setFitWidth(20);
        airSpeedIconView.setFitHeight(20);
        airSpeedLabel.setGraphic(airSpeedIconView);

    //Visibility and icon

        Label visibilityLabel = new Label("Visibility");
        visibilityLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD,12));
        visibilityLabel.setPadding(new Insets(0,10,0,10));
        Image visibilityIcon = new Image(getClass().getResourceAsStream("/visibility.png"));
        ImageView visibilityIconView = new ImageView(visibilityIcon);
        visibilityIconView .setFitWidth(20);
        visibilityIconView .setFitHeight(20);
        visibilityLabel.setGraphic(visibilityIconView);
        
        airDetails.getChildren().addAll(humidityLabel, airSpeedLabel,visibilityLabel);

        leftVBox.getChildren().addAll(searchItem, cityNameLabel,currentWeatherLabel,feelsLikeLabel);
        leftVBox.setMargin(searchButton, new javafx.geometry.Insets(5, 0, 0, 5));
        
        leftVBox.getChildren().add(tempDetails);
        
        leftVBox.getChildren().add(airDetails);
        
        updateWeatherInformation("Tampere",cityNameLabel, iconImageView, temperatureLabel, currentWeatherLabel,
                        feelsLikeLabel,humidityLabel, airSpeedLabel, visibilityLabel,dateTimeLabel);

        searchButton.setOnAction(event -> {
           String cityName1 = searchBar.getText().trim();
            cityName = cityName1;
            System.out.println("City Name Updated: " + cityName);
            updateCityName(cityName1);
            updateWeatherInformation();
            updateTabsContent();

            if(!cityName.isEmpty()) {
                System.out.println(cityName);
                updateWeatherInformation(cityName, cityNameLabel, iconImageView, temperatureLabel, currentWeatherLabel,
                        feelsLikeLabel, humidityLabel, airSpeedLabel, visibilityLabel, dateTimeLabel);




            }
        });
        
        return leftVBox;
    }

    public void updateCityName(String newCityName){
        this.cityName = newCityName;

    }

    private void updateWeatherInformation() {

        try {

            JsonObject jsonResponse = WeatherApiImpl.fetchWeatherData(cityName);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private HBox getBottomHBox() {
        HBox bottomHBox = new HBox();
        bottomHBox.setPrefHeight(400);
        bottomHBox.setAlignment(Pos.TOP_CENTER);  // for name of the city
        bottomHBox.setPadding(new Insets(30,5,0,5));


        // Create a TabPane to hold daily and hourly forecasts
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE); // Optional: Disable tab closing

        // Create tabs for daily and hourly forecasts
        dailyTab = new Tab("Daily Forecast");
        hourlyTab = new Tab("Hourly Forecast");
        mapTab = new Tab("Map");
        historyTab = new Tab("History");


        String queryString = "q=" + cityName;
        String completeURL = FORECAST_API_ENDPOINT + "?" + queryString + "&appid=" + API_KEY;
        System.out.println(completeURL);

        // Create VBox for daily forecast and get the forecast content
        printDailyForecast(completeURL);


        HourlyForecastHandler hourlyForecastHandler = new HourlyForecastHandler(completeURL);
        ListView<ModelHourlyWeatherItem> hourlyWeatherItemListView = hourlyForecastHandler.getHourlyForecastListView();
        hourlyTab.setContent(hourlyWeatherItemListView);



       // handleHistoryTabSelection();

        // Add tabs to the TabPane
        tabPane.getTabs().addAll(dailyTab, hourlyTab, mapTab, historyTab);
        bottomHBox.getChildren().addAll(tabPane);

        // Handle the event when the history tab is selected
        historyTab.setOnSelectionChanged(event -> handleHistoryTabSelection());




        return bottomHBox;
    }

    private void handleHistoryTabSelection() {
        System.out.println("Handle history tab selection");

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

                Label locationLabel = new Label("Location: " + entry.getCityName());
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



    private void updateTabsContent() {
        String queryString = "q=" + cityName;
        String completeURL = FORECAST_API_ENDPOINT + "?" + queryString + "&appid=" + API_KEY;

        Platform.runLater(() -> {

        // Create VBox for daily forecast and get the forecast content
            printDailyForecast(completeURL);
            handleHistoryTabSelection();


        // Create VBox for hourly forecast and get the forecast content
            HourlyForecastHandler hourlyForecastHandler = new HourlyForecastHandler(completeURL);
            ListView<ModelHourlyWeatherItem> hourlyWeatherItemListView = hourlyForecastHandler.getHourlyForecastListView();
            hourlyTab.setContent(hourlyWeatherItemListView);
    });
    }

    public void printDailyForecast(String completeURL){



        DailyForecastHandler dailyForecastHandler = new DailyForecastHandler(completeURL);


        List<ModelDailyWeatherItem> items = new ArrayList<>();
        items = dailyForecastHandler.getDailyForecast();

        ObservableList<ModelDailyWeatherItem> observableItems = FXCollections.observableArrayList(items);

        ListView<ModelDailyWeatherItem> dailyWeatherItemListView = new ListView<>(observableItems);

        //dailyWeatherItemListView.setPrefWidth(100);
        dailyWeatherItemListView.setPrefHeight(400);
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

                    ImageView imageView = dailyForecastHandler .createWeatherIconImageView(item.getWeatherIcon());
                    VBox graphicContainer = new VBox(10, imageView);

                    //graphicContainer.setPrefWidth(80);
                  //  graphicContainer.setPrefHeight(100);

                    graphicContainer.setAlignment(Pos.CENTER);

                    String minTemp = String.format("%.1f",item.getMinTemp());
                    String maxTemp = String.format("%.1f",item.getMaxTemp());


                    Label dateLabel = new Label(item.getDate());
                    dateLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 16));
                    dateLabel.setTextFill(Color.BLACK);
                    graphicContainer.getChildren().add(dateLabel);

                    setGraphic(graphicContainer);


                    Label temperatureLabel = new Label(minTemp + " / " + maxTemp);
                    temperatureLabel.setFont(Font.font(FONT_COMMON, FontWeight.SEMI_BOLD, 16));
                    temperatureLabel.setTextFill(Color.BLACK);
                   // setStyle("-fx-background-color: #b1c2d4;");

                    //the main color it is showing for each list


                    setStyle("-fx-background-color: #1FBED6;-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");


                    graphicContainer.getChildren().add(temperatureLabel);
                }
            }
        });


    }



    private void showOpenWeatherMap(WebEngine webEngine) {
        // Load OpenWeatherMap HTML into the WebView
        String openWeatherMapURL = getClass().getResource("OpenWeatherMap.html").toExternalForm();
        webEngine.load(openWeatherMapURL);
    }




    private Button getQuitButton() {
        //Creating a button.
        Button button = new Button("Quit");
        
        //Adding an event to the button to terminate the application.
        button.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });
        
        return button;
    }
    
    private void updateWeatherInformation(String cityName,Label cityNameLabel, ImageView iconImageView, Label temperatureLabel, Label currentWeatherLabel, Label feelsLikeLabel, Label humidityLabel,
                                          Label airSpeedLabel, Label visibilityLabel, Label dateTimeLabel) {
        try {

            JsonObject jsonResponse = WeatherApiImpl.fetchWeatherData(cityName);
            System.out.println(jsonResponse);

            if (jsonResponse != null) {

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


                double lat = jsonResponse.getAsJsonObject("coord").get("lat").getAsDouble();
                double lon = jsonResponse.getAsJsonObject("coord").get("lon").getAsDouble();
                // Update labels

                long timestamp = jsonResponse.get("dt").getAsLong();
                String dateTime = formatTimestamp(timestamp); // Implement the method to format timestamp
                dateTimeLabel.setText(dateTime);
                cityNameLabel.setText(city);

                String iconUrl = ICON_URL+weatherIcon+".png";
                Image iconImage = new Image(iconUrl);
                iconImageView.setImage(iconImage);

                int tempInt = (int) (temperature - 273.15);
                temperatureLabel.setText(tempInt + "°C");

                currentWeatherLabel.setText(weatherDescription);

                int feelsLikeInt = (int) (feelsLike - 273.15);
                feelsLikeLabel.setText("Feels Like: "+feelsLikeInt+"°C");

                airSpeedLabel.setText("Wind Speed: "+windSpeed+" m/s");

                humidityLabel.setText("Humidity: "+ humidity+"%");



                visibilityLabel.setText("Visibility: "+ visiblity/1000.0+"km");

                WeatherHistoryEntry entry = new WeatherHistoryEntry(cityName, tempInt, dateTime);
                // Add the entry to the history
                weatherHistory.addToHistory(entry);

                // Update the history ListView
                weatherHistory.updateHistoryListView();

                // Write the updated history list to the file
                try {
                    weatherHistory.writeToFile("history.json");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error: Unable to fetch weather data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private String formatTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, hh:mma", Locale.ENGLISH);
        return dateTime.format(formatter);
    }
}