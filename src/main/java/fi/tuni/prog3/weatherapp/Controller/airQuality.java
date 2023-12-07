package fi.tuni.prog3.weatherapp.Controller;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * Represents a class for fetching air quality information using the OpenWeatherMap API.
 */
public class airQuality {

    /**
     * The API endpoint for air quality information based on latitude and longitude.
     */
    private static final String API_ENDPOINT_LAT_LON = "https://api.openweathermap.org/data/2.5/air_pollution?";

    /**
     * The API key for accessing the OpenWeatherMap API.
     */
    private static final String API_KEY = "23a54643d49faf711fbbd48521054055";

    /**
     * Constructs a new airQuality object.
     */
    public airQuality() {
    }


    /**
     * Fetches air quality information based on the provided latitude and longitude.
     *
     * @param lat The latitude of the location.
     * @param lon The longitude of the location.
     * @return A string indicating the air quality level (e.g., "Good", "Moderate", "Unhealthy").
     *         If unable to determine air quality, returns "Unable to determine air quality".
     */
    public static String getAirQuality(double lat, double lon) {

        String latNew = Double.toString(lat);
        String lonNew = Double.toString(lon);

        try {
            String queryString = "lat="+latNew+"&lon="+lonNew;
            String completeURL = API_ENDPOINT_LAT_LON + queryString+"&appid="+API_KEY;

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


                int air_value = jsonResponse.getAsJsonArray("list").get(0).getAsJsonObject().getAsJsonObject("main").get("aqi").getAsInt();

                if(air_value  >= 0 && air_value  <= 50) return "Good";
                else if(air_value  >= 51 && air_value  <= 100) return "Moderate";
                else if(air_value  >= 101 && air_value  <= 150) return "Unhealthy for sensitive groups";
                else if(air_value  >= 151 && air_value  <= 200) return "Unhealthy";
                else if(air_value  >= 201 && air_value  <= 300) return "Very Unhealthy";
                else if(air_value  >= 301 && air_value  <= 500) return "Hazardous";

            } else {
                System.out.println("Error: Unable to fetch weather data");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Unable to determine air quality";
    }



}
