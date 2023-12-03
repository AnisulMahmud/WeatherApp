package fi.tuni.prog3.weatherapp;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherApiImpl implements iAPI {

    private static final String API_KEY = "23a54643d49faf711fbbd48521054055";
    private static final String API_ENDPOINT = "http://api.openweathermap.org/data/2.5/weather";


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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String lookUpLocation(String loc) {
        // Implement logic to look up location coordinates
        // ...

        return "latitude,longitude"; // Replace with actual coordinates
    }

    @Override
    public String getCurrentWeather(double lat, double lon) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    @Override
    public String getForecast(double lat, double lon) {
        // Implement logic to get forecast
        // ...

        return "Forecast data"; // Replace with actual forecast data
    }
}