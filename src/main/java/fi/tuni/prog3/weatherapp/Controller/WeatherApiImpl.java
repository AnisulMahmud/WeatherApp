package fi.tuni.prog3.weatherapp.Controller;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.format.DateTimeParseException;

/**
 * Implementation of the {@code iAPI} interface for interacting with the OpenWeatherMap API.
 * Provides methods to fetch weather data and look up locations.
 */
public class WeatherApiImpl implements iAPI {

    private static final String API_KEY = "23a54643d49faf711fbbd48521054055";
    private static final String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather";


    /**
     * Fetches weather data for the specified city from the OpenWeatherMap API.
     *
     * @param cityName The name of the city for which to fetch weather data.
     * @return A {@link JsonObject} containing the weather data, or {@code null} if an error occurs.
     * @throws IOException            If an I/O exception occurs while connecting to the API or reading the response.
     * @throws JsonParseException     If an error occurs while parsing the JSON response.
     * @throws DateTimeParseException If an error occurs while parsing the date and time from the timestamp.
     */
    public static JsonObject fetchWeatherData(String cityName) {
        try {
            String queryString = "q=" + cityName;
            String completeURL = API_ENDPOINT + "?" + queryString + "&appid=" + API_KEY;

            URL url = new URL(completeURL);
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

                // Parse JSON response
                Gson gson = new Gson();
                return gson.fromJson(response.toString(), JsonObject.class);
            } else {
                System.out.println("Error: Unable to fetch weather data");
                return null;
            }
        } catch (IOException | JsonParseException | DateTimeParseException e) {
            e.printStackTrace();
            return  null;
        }
    }
    @Override
    public String lookUpLocation(String loc) {

        return "latitude,longitude"; // Replace with actual coordinates
    }

    /**
     * Retrieves the current weather data for the specified coordinates from the OpenWeatherMap API.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A string containing the current weather information, or an error message if fetching fails.
     * @throws JsonParseException     If an error occurs while parsing the JSON response.
     * @throws DateTimeParseException If an error occurs while parsing the date and time from the timestamp.
     */
    @Override
    public String getCurrentWeather(double lat, double lon) throws JsonParseException, DateTimeParseException {
        try {
            String queryString = "lat=" + lat + "&lon=" + lon;
            String completeURL = API_ENDPOINT + "?" + queryString + "&appid=" + API_KEY;

            URL url = new URL(completeURL);
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

                // Parse JSON response
                Gson gson = new Gson();
                JsonObject jsonResponse = gson.fromJson(response.toString(), JsonObject.class);

                System.out.println(jsonResponse);

                // Extract relevant information (you can return this data as needed)
                String city = jsonResponse.get("name").getAsString();
                double temperature = jsonResponse.getAsJsonObject("main").get("temp").getAsDouble();
                // Extract other information...

                return "Current weather data for " + city + ": Temperature - " + temperature; // Adjust as needed
            } else {
                System.out.println("Error: Unable to fetch weather data");
                return "Error: Unable to fetch weather data";
            }
        } catch (IOException | JsonParseException | DateTimeParseException e) {
            e.printStackTrace();

            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String getForecast(double lat, double lon) {

        return "Forecast data"; // Replace with actual forecast data
    }
}