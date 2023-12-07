package fi.tuni.prog3.weatherapp;
import fi.tuni.prog3.weatherapp.Model.ModelHourlyWeatherItem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Comparator;
import java.util.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;



public class HourlyForecastHandler {

    private static final String ICON_URL = "http://openweathermap.org/img/wn/";
    private final String hourlyApiEndpoint;
    private static final String FONT_COMMON = "SansSerif";

    public HourlyForecastHandler(String hourlyApiEndpoint) {
        this.hourlyApiEndpoint = hourlyApiEndpoint;
    }

    public List<ModelHourlyWeatherItem> getHourlyForecast() {
        List<ModelHourlyWeatherItem> hourlyWeatherList = new ArrayList<>();


        try {
            URL url = new URI(hourlyApiEndpoint).toURL();

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

                if (jsonResponse.isJsonObject()) {
                    JsonObject jsonObject = jsonResponse.getAsJsonObject();
                    if (jsonObject.has("list") && jsonObject.get("list").isJsonArray()) {
                        JsonArray hourlyForecastArray = jsonObject.getAsJsonArray("list");

                        for (JsonElement element : hourlyForecastArray) {
                            JsonObject hourlyData = element.getAsJsonObject();
                            long timestamp = hourlyData.get("dt").getAsLong();
                            LocalDateTime dateTime = Instant.ofEpochSecond(timestamp)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime();

                            double temperature = hourlyData.getAsJsonObject("main").get("temp_max").getAsDouble();
                            double minTemperature = hourlyData.getAsJsonObject("main").get("temp_min").getAsDouble();
                            String weatherIcon = hourlyData.getAsJsonArray("weather")
                                    .get(0).getAsJsonObject()
                                    .get("icon").getAsString();
                            String humidity = hourlyData.getAsJsonObject("main").get("humidity").toString();
                            String windSpeed = hourlyData.getAsJsonObject("wind").get("speed").getAsString();

                            ModelHourlyWeatherItem hourlyWeather = new ModelHourlyWeatherItem();
                            hourlyWeather.setDateTime(dateTime);
                            hourlyWeather.setWeatherIcon(weatherIcon);
                            hourlyWeather.setMaxTemp(temperature - 273.15);
                            hourlyWeather.setMinTemp(minTemperature - 273.15);
                            hourlyWeather.setHumidity(humidity);
                            hourlyWeather.setWindSpeed(String.valueOf(windSpeed));

                            hourlyWeatherList.add(hourlyWeather);
                        }
                    } else {
                        System.out.println("Error: 'list' element not found or is not a JsonArray");
                    }
                } else {
                    System.out.println("Error: Unable to fetch hourly forecast data");
                }
            } else {
                System.out.println("Error: Unable to fetch hourly forecast data");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hourlyWeatherList;
    }

    public ListView<ModelHourlyWeatherItem> getHourlyForecastListView() {
        List<ModelHourlyWeatherItem> hourlyWeatherList = getHourlyForecast();

        ObservableList<ModelHourlyWeatherItem> observableItems = FXCollections.observableArrayList(hourlyWeatherList);

        ListView<ModelHourlyWeatherItem> hourlyWeatherItemListView = new ListView<>(observableItems);
        hourlyWeatherItemListView.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        hourlyWeatherItemListView.setPrefWidth(600); // Adjust as needed
        hourlyWeatherItemListView.setPrefHeight(400);

//        hourlyWeatherItemListView.setStyle("-fx-background-image:url('/bottomBox.jpg');" +
//                "-fx-background-size: cover; " +
//                "-fx-background-position: center;");

        hourlyWeatherItemListView.setCellFactory(param -> new ListCell<ModelHourlyWeatherItem>() {
            @Override
            protected void updateItem(ModelHourlyWeatherItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ImageView imageView = createWeatherIconImageView(item.getWeatherIcon());

                    Label hourLabel = new Label(String.valueOf(item.getDateTime().getHour()));
                    hourLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));

                    Label tempLabel = new Label(String.format("%.1f°C", item.getMaxTemp()));
                    tempLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
                    Image tempIcon = new Image(getClass().getResourceAsStream("/feels_like.png"));
                    ImageView tempIconIconIconView = new ImageView(tempIcon);
                    tempIconIconIconView.setFitWidth(15);
                    tempIconIconIconView.setFitHeight(15);
                    tempLabel.setGraphic(tempIconIconIconView);

                    Label minTempLabel = new Label(String.format("%.1f°C", item.getMinTemp()));
                    minTempLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
                    Image minTempIcon = new Image(getClass().getResourceAsStream("/feels_like.png"));
                    ImageView minTempIconView = new ImageView(minTempIcon);
                    minTempIconView.setFitWidth(15);
                    minTempIconView.setFitHeight(15);
                    minTempLabel.setGraphic(minTempIconView);


                    Label humidityLabel = new Label(item.getHumidity() + "%");
                    humidityLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
                    Image humidityIcon = new Image(getClass().getResourceAsStream("/humidity.png"));
                    ImageView humidityIconIconView = new ImageView(humidityIcon);
                    humidityIconIconView.setFitWidth(20);
                    humidityIconIconView.setFitHeight(20);
                    humidityLabel.setGraphic(humidityIconIconView);

                    Label windSpeedLabel = new Label(String.format("%.1f m/s", Double.parseDouble(item.getWindSpeed())));
                    windSpeedLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));
                    Image windSpeedIcon = new Image(getClass().getResourceAsStream("/windspeed.png"));
                    ImageView windSpeedIconView = new ImageView(windSpeedIcon);
                    windSpeedIconView.setFitWidth(15);
                    windSpeedIconView.setFitHeight(15);
                    windSpeedLabel.setGraphic(windSpeedIconView);


                    Label arrowLabel = new Label("↓");
                    arrowLabel.setFont(Font.font(FONT_COMMON, FontWeight.BOLD, 12));

                    VBox hourlyVBox = new VBox(hourLabel, imageView, tempLabel, arrowLabel, minTempLabel, humidityLabel, windSpeedLabel);
                    hourlyVBox.setAlignment(Pos.CENTER);
                    hourlyVBox.setSpacing(15);
                    hourlyVBox.setAlignment(Pos.CENTER);

                    //VBox graphicContainer = new VBox(10); // Adjust spacing as needed
                   // graphicContainer.setPrefWidth(200);


                    HBox container = new HBox(hourlyVBox);
                    container.setAlignment(Pos.CENTER);
                    setStyle("-fx-background-color: #1FBED6;-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");
                    setGraphic(container);
                }
            }
        });

        return hourlyWeatherItemListView;
    }

    public ImageView createWeatherIconImageView(String weatherIcon) {
        ImageView weatherIconImageView = new ImageView(new Image(ICON_URL + weatherIcon + ".png"));
        weatherIconImageView.setFitWidth(50);
        weatherIconImageView.setFitHeight(50);

        return weatherIconImageView;
    }
}

//    private String formatTimestamp(long timestamp) {
//        Instant instant = Instant.ofEpochSecond(timestamp);
//        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh ", Locale.ENGLISH);
//        return dateTime.format(formatter);
//    }
