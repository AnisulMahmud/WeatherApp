package fi.tuni.prog3.weatherapp.Model;


import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.GsonBuilder;





/**
 * Represents a weather history entry with information about the city, country, temperature, and search time.
 */
public class WeatherHistoryEntry {

    /**
     * The name of the city.
     */
    @SerializedName("cityName")
    private String cityName;

    /**
     * The name of the country.
     */
    @SerializedName("countryName")
    private String countryName;

    /**
     * The temperature at the time of the weather search.
     */
    @SerializedName("temperature")
    private double temperature;

    /**
     * The time when the search for weather information was performed.
     */
    @SerializedName("searchTime")
    private String searchTime;

    /**
     * Constructs a new WeatherHistoryEntry with the specified city name, country name, temperature, and search time.
     *
     * @param cityName    The name of the city.
     * @param countryName  The name of the country.
     * @param temperature  The temperature at the time of the weather search.
     * @param searchTime   The time when the search for weather information was performed.
     */
    public WeatherHistoryEntry(String cityName, String countryName, double temperature, String searchTime) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.searchTime = searchTime;
        this.countryName = countryName;
    }

    /**
     * Gets the name of the city.
     *
     * @return The name of the city.
     */
    public String getCityName() {
        return cityName;
    }

    /**
     * Sets the name of the city.
     *
     * @param cityName The name of the city.
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     * Gets the name of the country.
     *
     * @return The name of the country.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the name of the country.
     *
     * @param countryName The name of the country.
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * Gets the temperature at the time of the weather search.
     *
     * @return The temperature at the time of the weather search.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature at the time of the weather search.
     *
     * @param temperature The temperature at the time of the weather search.
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Gets the time when the search for weather information was performed.
     *
     * @return The time when the search for weather information was performed.
     */
    public String getSearchTime() {
        return searchTime;
    }

    /**
     * Sets the time when the search for weather information was performed.
     *
     * @param searchTime The time when the search for weather information was performed.
     */
    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    /**
     * Gets a custom Gson object for pretty printing.
     *
     * @return A custom Gson object for pretty printing.
     */
    public static Gson getCustomGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
