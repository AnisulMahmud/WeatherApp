package fi.tuni.prog3.weatherapp;
import fi.tuni.prog3.weatherapp.Model.ModelDailyWeatherItem;



import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.scene.layout.Region;
import java.util.Locale;
import java.time.LocalDate;

public class DailyForecastHandler {

    private static final String ICON_URL = "http://openweathermap.org/img/wn/";
    private final String dailyApiEndpoint;

    public DailyForecastHandler(String dailyApiEndpoint) {
        this.dailyApiEndpoint = dailyApiEndpoint;
    }

    public List<ModelDailyWeatherItem> getDailyForecast() {
        VBox dailyForecastVBox = new VBox();
        dailyForecastVBox.setAlignment(Pos.TOP_LEFT);
        dailyForecastVBox.setPadding(new Insets(10, 10, 10, 10));
//        dailyForecastVBox.setStyle("-fx-background-image:url('/2.jpg');" +
//                "-fx-background-size: cover; " +
//                "-fx-background-position: center;");

        List<ModelDailyWeatherItem> dailyWeatherList = new ArrayList<>();

        try {
            URL url = new URI(dailyApiEndpoint).toURL();

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonElement jsonResponse = JsonParser.parseString(response.toString());

                System.out.println(jsonResponse);

                if (jsonResponse.isJsonObject()) {
                    JsonArray dailyForecastArray = jsonResponse.getAsJsonObject().getAsJsonArray("list");

                    dailyForecastVBox.getChildren().clear();

                    if (dailyForecastArray != null) {
                        Set<String> uniqueDates = new HashSet<>();

                        // Create HBoxes for each aspect (days, icons, min/max temperatures)
                        HBox daysRow = new HBox();
                        daysRow.setAlignment(Pos.TOP_LEFT);

                        HBox iconsRow = new HBox();
                        iconsRow.setAlignment(Pos.TOP_LEFT);

                        HBox tempRow = new HBox();
                        tempRow.setAlignment(Pos.TOP_LEFT);

                        int count =0;

                        for (JsonElement element : dailyForecastArray) {
                            count++;


//                            if (count>48) {
//                                continue; // Skip duplicate dates
//                            }
                            JsonObject dailyData = element.getAsJsonObject();
                            long timestamp = dailyData.get("dt").getAsLong();
                            String date = formatDailyTimestamp(timestamp);

                            // Check if the date has already been added
                            if (uniqueDates.contains(date)) {
                                continue; // Skip duplicate dates
                            }


                            uniqueDates.add(date);

                            String weatherIcon = dailyData.getAsJsonArray("weather")
                                    .get(0).getAsJsonObject()
                                    .get("icon").getAsString();
                            double minTemp = dailyData.getAsJsonObject("main").get("temp_min").getAsDouble();
                            double maxTemp = dailyData.getAsJsonObject("main").get("temp_max").getAsDouble();

                            ModelDailyWeatherItem dailyWeather = new ModelDailyWeatherItem();
                            dailyWeather.setDate(date);
                            dailyWeather.setWeatherIcon(weatherIcon);
                            dailyWeather.setMinTemp(minTemp - 273.15);
                            dailyWeather.setMaxTemp(maxTemp - 273.15);

                            dailyWeatherList.add(dailyWeather);

                        }
                    } else {
                        System.out.println("Error: Daily forecast array is null");
                    }
                } else {
                    System.out.println("Error: Unable to fetch daily forecast data");
                }
            } else {
                System.out.println("Error: Unable to fetch daily forecast data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dailyWeatherList;
    }








    public ImageView createWeatherIconImageView(String weatherIcon) {
        ImageView weatherIconImageView = new ImageView(new Image(ICON_URL + weatherIcon + ".png"));
        weatherIconImageView.setFitWidth(50);
        weatherIconImageView.setFitHeight(50);

        return weatherIconImageView;
    }



    private String formatDailyTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E dd.MM", Locale.ENGLISH);
        return dateTime.format(formatter);
    }

}