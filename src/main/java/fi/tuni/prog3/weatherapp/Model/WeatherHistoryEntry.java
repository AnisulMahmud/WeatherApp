package fi.tuni.prog3.weatherapp.Model;

import java.time.LocalDateTime;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;




public class WeatherHistoryEntry {
    @SerializedName("cityName")
    private String cityName;

    @SerializedName("temperature")
    private double temperature;

    @SerializedName("searchTime")
    private String searchTime;

    public WeatherHistoryEntry(String cityName, double temperature, String searchTime) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.searchTime = searchTime;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

    public String toJson() {
        Gson gson = getCustomGson();
        return gson.toJson(this);
    }

    public static Gson getCustomGson() {
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
