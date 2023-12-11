package fi.tuni.prog3.weatherapp.Controller;
import com.google.gson.*;
import fi.tuni.prog3.weatherapp.Model.ModelHourlyWeatherItem;


import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;



/**
 * Handles the retrieval and display of hourly weather forecast data.
 */
public class HourlyForecastHandler {

    /**
     * The base URL for weather icon images.
     */
    private static final String ICON_URL = "http://openweathermap.org/img/wn/";

    /**
     * The API endpoint for the hourly weather forecast.
     */
    private final String hourlyApiEndpoint;

    /**
     * The common font used in the UI.
     */
    private static final String FONT_COMMON = "SansSerif";

    /**
     * Constructs a new HourlyForecastHandler with the specified API endpoint.
     *
     * @param hourlyApiEndpoint The API endpoint for the hourly weather forecast.
     */
    public HourlyForecastHandler(String hourlyApiEndpoint) {
        this.hourlyApiEndpoint = hourlyApiEndpoint;
    }

    /**
     * Retrieves the hourly weather forecast data from the specified API endpoint.
     *
     * @return A list of {@link ModelHourlyWeatherItem} representing the hourly weather forecast.
     * @throws IOException            If an I/O exception occurs while connecting to the API or reading the response.
     * @throws JsonParseException     If an error occurs while parsing the JSON response.
     * @throws DateTimeParseException If an error occurs while parsing the date and time from the timestamp.
     */
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

        } catch (IOException | JsonParseException | URISyntaxException | DateTimeParseException e) {
            e.printStackTrace();
        }

        return hourlyWeatherList;
    }
    /**
     * Creates and returns a ListView of ModelHourlyWeatherItem for displaying the hourly weather forecast.
     *
     * @return A ListView containing ModelHourlyWeatherItem for the hourly weather forecast.
     */

    public ListView<ModelHourlyWeatherItem> getHourlyForecastListView() {
        List<ModelHourlyWeatherItem> hourlyWeatherList = getHourlyForecast();

        ObservableList<ModelHourlyWeatherItem> observableItems = FXCollections.observableArrayList(hourlyWeatherList);

        ListView<ModelHourlyWeatherItem> hourlyWeatherItemListView = new ListView<>(observableItems);
        hourlyWeatherItemListView.setOrientation(javafx.geometry.Orientation.HORIZONTAL);
        hourlyWeatherItemListView.setPrefWidth(600); // Adjust as needed
        hourlyWeatherItemListView.setPrefHeight(400);



        hourlyWeatherItemListView.setCellFactory(param -> new ListCell<ModelHourlyWeatherItem>() {
            @Override
            protected void updateItem(ModelHourlyWeatherItem item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ImageView imageView = createWeatherIconImageView(item.getWeatherIcon());



                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma");
                    Label hourLabel = new Label(item.getDateTime().format(formatter));
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
                    setStyle("-fx-background-color: #099898;-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");
                    setGraphic(container);
                }
            }
        });

        return hourlyWeatherItemListView;
    }

    /**
     * Creates an ImageView for a weather icon based on the provided weather icon code.
     *
     * @param weatherIcon The weather icon code.
     * @return An ImageView for the specified weather icon.
     */
    public ImageView createWeatherIconImageView(String weatherIcon) {
        ImageView weatherIconImageView = new ImageView(new Image(ICON_URL + weatherIcon + ".png"));
        weatherIconImageView.setFitWidth(50);
        weatherIconImageView.setFitHeight(50);

        return weatherIconImageView;
    }

    /**
     * Formats a Unix timestamp into a human-readable time.
     *
     * @param unixTimestamp The Unix timestamp to format.
     * @return A formatted time string.
     */
    private String formatTimestamp(long unixTimestamp) {
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mma", Locale.ENGLISH);
        return dateTime.format(formatter);
    }

}