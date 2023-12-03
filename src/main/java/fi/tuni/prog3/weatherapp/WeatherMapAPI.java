package fi.tuni.prog3.weatherapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherMapAPI implements iAPI {

    private static final String API_KEY = "52eaeb6f9d57bf484203a9956f4ed911";  
    private static final String API_ENDPOINT = "http://api.openweathermap.org/data/2.5";
            
    @Override
    public String lookUpLocation(String loc) {
        // Implement location lookup if necessary
        return null;
    }

    @Override
    public String getCurrentWeather(double lat, double lon) {
        String apiUrl = API_ENDPOINT + "/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;
        return sendHttpRequest(apiUrl);
    }

    @Override
    public String getForecast(double lat, double lon) {
        String apiUrl = API_ENDPOINT + "/forecast?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;
        return sendHttpRequest(apiUrl);
    }

    private String sendHttpRequest(String apiUrl) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        return response.toString();
    }
}
