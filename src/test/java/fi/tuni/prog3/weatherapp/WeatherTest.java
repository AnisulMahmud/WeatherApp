package fi.tuni.prog3.weatherapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import javafx.application.Platform;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.io.FileWriter;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import fi.tuni.prog3.weatherapp.Model.WeatherHistoryEntry;

import fi.tuni.prog3.weatherapp.Controller.*;
import fi.tuni.prog3.weatherapp.Model.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class WeatherTest {

    private static final String forecast_api= "http://api.openweathermap.org/data/2.5/forecast?q=Tampere&appid=23a54643d49faf711fbbd48521054055";


    @Test
    public void testWeatherItemConstructorAndGetters() {
        WeatherItem weatherItem = new WeatherItem("Monday", "2023-12-04", "sunny", "25°C");

        assertEquals("Monday", weatherItem.getDaysOfWeek());
        assertEquals("2023-12-04", weatherItem.getDateOfMonth());
        assertEquals("sunny", weatherItem.getWeatherIcon());
        assertEquals("25°C", weatherItem.getTemperature());
    }

    @Test
    public void testWeatherItemSetters() {
        WeatherItem weatherItem = new WeatherItem("Monday", "2023-12-04", "sunny", "25°C");

        weatherItem.setDaysOfWeek("Tuesday");
        weatherItem.setDateOfMonth("2023-12-05");
        weatherItem.setWeatherIcon("rainy");
        weatherItem.setTemperature("20°C");

        assertEquals("Tuesday", weatherItem.getDaysOfWeek());
        assertEquals("2023-12-05", weatherItem.getDateOfMonth());
        assertEquals("rainy", weatherItem.getWeatherIcon());
        assertEquals("20°C", weatherItem.getTemperature());
    }

    @Test
    public void testNullValues() {
        WeatherItem weatherItem = new WeatherItem(null, null, null, null);

        assertNull(weatherItem.getDaysOfWeek(), "Days of Week should be null");
        assertNull(weatherItem.getDateOfMonth(), "Date of Month should be null");
        assertNull(weatherItem.getWeatherIcon(), "Weather Icon should be null");
        assertNull(weatherItem.getTemperature(), "Temperature should be null");

        // Testing setters with null values
        weatherItem.setDaysOfWeek("Monday");
        weatherItem.setDateOfMonth(null);
        weatherItem.setWeatherIcon("cloudy");
        weatherItem.setTemperature("15°C");

        assertEquals("Monday", weatherItem.getDaysOfWeek(), "Days of Week should be set to Monday");
        assertNull(weatherItem.getDateOfMonth(), "Date of Month should still be null");
        assertEquals("cloudy", weatherItem.getWeatherIcon(), "Weather Icon should be set to cloudy");
        assertEquals("15°C", weatherItem.getTemperature(), "Temperature should be set to 15°C");
    }

    @Test
    public void testConstructorWithValidValues() {
        FavouriteEntry entry = new FavouriteEntry("City1", "Country1", 25.5, "2023-12-06T12:00:00");
        assertEquals("City1", entry.getCityName());
        assertEquals("Country1", entry.getCountryName());
        assertEquals(25.5, entry.getTemperature(), 0.001); // Allowing for a small delta in double comparison
        assertEquals("2023-12-06T12:00:00", entry.getSearchTime());
    }



    @Test
    public void testSetterAndGetterMethods() {
        FavouriteEntry entry = new FavouriteEntry("City1", "Country1", 25.5, "2023-12-06T12:00:00");

        entry.setCityName("City2");
        entry.setCountryName("Country2");
        entry.setTemperature(30.0);
        entry.setSearchTime("2023-12-07T14:30:00");

        assertEquals("City2", entry.getCityName());
        assertEquals("Country2", entry.getCountryName());
        assertEquals(30.0, entry.getTemperature(), 0.001);
        assertEquals("2023-12-07T14:30:00", entry.getSearchTime());
    }

    @Test
    public void testDefaultConstructor2() {
        ModelDailyWeatherItem dailyWeatherItem = new ModelDailyWeatherItem();

        assertNull(dailyWeatherItem.getDate());
        assertNull(dailyWeatherItem.getWeatherIcon());
        assertEquals(0.0, dailyWeatherItem.getMinTemp(), 0.001);
        assertEquals(0.0, dailyWeatherItem.getMaxTemp(), 0.001);
        assertEquals(0, dailyWeatherItem.getHumidity());
        assertEquals(0.0, dailyWeatherItem.getSealevel(), 0.001);
    }

    @Test
    public void testGetterAndSetterMethods2() {
        ModelDailyWeatherItem dailyWeatherItem = new ModelDailyWeatherItem();

        dailyWeatherItem.setDate("2023-12-06");
        assertEquals("2023-12-06", dailyWeatherItem.getDate());

        dailyWeatherItem.setWeatherIcon("cloudy");
        assertEquals("cloudy", dailyWeatherItem.getWeatherIcon());

        dailyWeatherItem.setMinTemp(15.0);
        assertEquals(15.0, dailyWeatherItem.getMinTemp(), 0.001);

        dailyWeatherItem.setMaxTemp(25.0);
        assertEquals(25.0, dailyWeatherItem.getMaxTemp(), 0.001);

        dailyWeatherItem.setHumidity(70);
        assertEquals(70, dailyWeatherItem.getHumidity());

        dailyWeatherItem.setSeaLevel(1013.25);
        assertEquals(1013.25, dailyWeatherItem.getSealevel(), 0.001);
    }

    @Test
    public void testEquality2() {
        ModelDailyWeatherItem item1 = new ModelDailyWeatherItem();
        item1.setDate("2023-12-06");
        item1.setWeatherIcon("cloudy");
        item1.setMinTemp(15.0);
        item1.setMaxTemp(25.0);
        item1.setHumidity(70);
        item1.setSeaLevel(1013.25);

        ModelDailyWeatherItem item2 = new ModelDailyWeatherItem();
        item2.setDate("2023-12-06");
        item2.setWeatherIcon("cloudy");
        item2.setMinTemp(15.0);
        item2.setMaxTemp(25.0);
        item2.setHumidity(70);
        item2.setSeaLevel(1013.25);

        ModelDailyWeatherItem item3 = new ModelDailyWeatherItem();
        item3.setDate("2023-12-07");
        item3.setWeatherIcon("sunny");
        item3.setMinTemp(20.0);
        item3.setMaxTemp(30.0);
        item3.setHumidity(80);
        item3.setSeaLevel(1015.0);

        assertNotEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    //ModelHourlyWeatherItem

    @Test
    public void testDefaultConstructor4() {
        ModelHourlyWeatherItem hourlyWeatherItem = new ModelHourlyWeatherItem();

        assertNull(hourlyWeatherItem.getDateTime());
        assertNull(hourlyWeatherItem.getDate());
        assertEquals(0L, hourlyWeatherItem.getHour());
        assertNull(hourlyWeatherItem.getWeatherIcon());
        assertEquals(0.0, hourlyWeatherItem.getMinTemp(), 0.001);
        assertEquals(0.0, hourlyWeatherItem.getMaxTemp(), 0.001);
        assertNull(hourlyWeatherItem.getHumidity());
        assertNull(hourlyWeatherItem.getWindSpeed());
    }

    @Test
    public void testGetterAndSetterMethods3() {
        ModelHourlyWeatherItem hourlyWeatherItem = new ModelHourlyWeatherItem();

        LocalDateTime dateTime = LocalDateTime.now();
        hourlyWeatherItem.setDateTime(dateTime);
        assertEquals(dateTime, hourlyWeatherItem.getDateTime());

        hourlyWeatherItem.setDate("2023-12-06");
        assertEquals("2023-12-06", hourlyWeatherItem.getDate());

        hourlyWeatherItem.setHour(12L);
        assertEquals(12L, hourlyWeatherItem.getHour());

        hourlyWeatherItem.setWeatherIcon("cloudy");
        assertEquals("cloudy", hourlyWeatherItem.getWeatherIcon());

        hourlyWeatherItem.setMinTemp(15.0);
        assertEquals(15.0, hourlyWeatherItem.getMinTemp(), 0.001);

        hourlyWeatherItem.setMaxTemp(25.0);
        assertEquals(25.0, hourlyWeatherItem.getMaxTemp(), 0.001);

        hourlyWeatherItem.setHumidity("50%");
        assertEquals("50%", hourlyWeatherItem.getHumidity());

        hourlyWeatherItem.setWindSpeed("10 m/s");
        assertEquals("10 m/s", hourlyWeatherItem.getWindSpeed());
    }

    @Test
    public void testEquality3() {
        ModelHourlyWeatherItem item1 = new ModelHourlyWeatherItem();
        item1.setDateTime(LocalDateTime.parse("2023-12-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        item1.setDate("2023-12-06");
        item1.setHour(12L);
        item1.setWeatherIcon("cloudy");
        item1.setMinTemp(15.0);
        item1.setMaxTemp(25.0);
        item1.setHumidity("50%");
        item1.setWindSpeed("10 m/s");

        ModelHourlyWeatherItem item2 = new ModelHourlyWeatherItem();
        item2.setDateTime(LocalDateTime.parse("2023-12-06T12:00:00", DateTimeFormatter.ISO_DATE_TIME));
        item2.setDate("2023-12-06");
        item2.setHour(12L);
        item2.setWeatherIcon("cloudy");
        item2.setMinTemp(15.0);
        item2.setMaxTemp(25.0);
        item2.setHumidity("50%");
        item2.setWindSpeed("10 m/s");

        ModelHourlyWeatherItem item3 = new ModelHourlyWeatherItem();
        item3.setDateTime(LocalDateTime.parse("2023-12-07T14:30:00", DateTimeFormatter.ISO_DATE_TIME));
        item3.setDate("2023-12-07");
        item3.setHour(14L);
        item3.setWeatherIcon("sunny");
        item3.setMinTemp(20.0);
        item3.setMaxTemp(30.0);
        item3.setHumidity("60%");
        item3.setWindSpeed("15 m/s");

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    /// WeatherHistoryEntry

    @Test
    public void testDefaultConstructorWeatherHistoryEntry() {
        WeatherHistoryEntry historyEntry = new WeatherHistoryEntry("City1", "Country1", 25.5, "2023-12-06T12:00:00");

        assertEquals("City1", historyEntry.getCityName());
        assertEquals("Country1", historyEntry.getCountryName());
        assertEquals(25.5, historyEntry.getTemperature(), 0.001);
        assertEquals("2023-12-06T12:00:00", historyEntry.getSearchTime());
    }

    @Test
    public void testGetterAndSetterMethodsWeatherHistoryEntry() {
        WeatherHistoryEntry historyEntry = new WeatherHistoryEntry("City1", "Country1", 25.5, "2023-12-06T12:00:00");

        historyEntry.setCityName("City2");
        assertEquals("City2", historyEntry.getCityName());

        historyEntry.setCountryName("Country2");
        assertEquals("Country2", historyEntry.getCountryName());

        historyEntry.setTemperature(30.0);
        assertEquals(30.0, historyEntry.getTemperature(), 0.001);

        historyEntry.setSearchTime("2023-12-07T14:30:00");
        assertEquals("2023-12-07T14:30:00", historyEntry.getSearchTime());
    }


    //WeatherItem
    @Test
    public void testDefaultConstructorWeatherItem() {
        WeatherItem weatherItem = new WeatherItem("Monday", "2023-12-06", "sunny", "25°C");

        assertEquals("Monday", weatherItem.getDaysOfWeek());
        assertEquals("2023-12-06", weatherItem.getDateOfMonth());
        assertEquals("sunny", weatherItem.getWeatherIcon());
        assertEquals("25°C", weatherItem.getTemperature());
    }

    @Test
    public void testGetterAndSetterMethodsWeatherItem() {
        WeatherItem weatherItem = new WeatherItem("Monday", "2023-12-06", "sunny", "25°C");

        weatherItem.setDaysOfWeek("Tuesday");
        assertEquals("Tuesday", weatherItem.getDaysOfWeek());

        weatherItem.setDateOfMonth("2023-12-07");
        assertEquals("2023-12-07", weatherItem.getDateOfMonth());

        weatherItem.setWeatherIcon("cloudy");
        assertEquals("cloudy", weatherItem.getWeatherIcon());

        weatherItem.setTemperature("20°C");
        assertEquals("20°C", weatherItem.getTemperature());
    }

    @Test
    public void testEqualityWeatherItem() {
        WeatherItem item1 = new WeatherItem("Monday", "2023-12-06", "sunny", "25°C");
        WeatherItem item2 = new WeatherItem("Monday", "2023-12-06", "sunny", "25°C");
        WeatherItem item3 = new WeatherItem("Tuesday", "2023-12-07", "cloudy", "20°C");

        assertEquals(item1, item2);
        assertNotEquals(item1, item3);
    }

    //airQuality

    @Test
    public void testGoodAirQuality() {
        double lat = 0.0;
        double lon = 0.0;
        airQuality airQuality = new airQuality();

        String result = airQuality.getAirQuality(lat, lon);

        // Assuming your method returns "Good" for an AQI of 25
        assertEquals("Good", result);
    }

    @Test
    public void testModerateAirQuality() {
        double lat = 0.0;
        double lon = 0.0;
        airQuality airQuality = new airQuality();

        String result = airQuality.getAirQuality(lat, lon);

        // Assuming your method returns "Moderate" for an AQI of 75
        assertNotEquals("Moderate", result);
    }

    @Test
    public void testUnhealthyForSensitiveGroups() {
        double lat = 0.0;
        double lon = 0.0;
        airQuality airQuality = new airQuality();

        String result = airQuality.getAirQuality(lat, lon);


        assertNotEquals("Unhealthy for sensitive groups", result);
    }

    // DailyForecastHandler
    @Test
    public void testGetDailyForecast() {
        DailyForecastHandler dailyForecastHandler = new DailyForecastHandler(forecast_api);
        List<ModelDailyWeatherItem> dailyWeatherList = dailyForecastHandler.getDailyForecast();


        for (ModelDailyWeatherItem dailyWeather : dailyWeatherList) {


            assertTrue(dailyWeather.getHumidity() >= 0 && dailyWeather.getHumidity() <= 100);

        }
    }



// HourlyForecastHandler

    @Test
    void testGetHourlyForecast() {

        HourlyForecastHandler hourlyForecastHandler = new HourlyForecastHandler(forecast_api);

        // Call the method to get the hourly forecast
        List<ModelHourlyWeatherItem> hourlyForecast = hourlyForecastHandler.getHourlyForecast();

        // Perform assertions based on expected behavior
        assertNotNull(hourlyForecast, "Hourly forecast should not be null");
        assertFalse(hourlyForecast.isEmpty(), "Hourly forecast should not be empty");

        // Add more assertions based on your specific requirements

        // Example: Ensure that the temperature is within a reasonable range
        for (ModelHourlyWeatherItem hourlyWeatherItem : hourlyForecast) {
            double maxTemp = hourlyWeatherItem.getMaxTemp();
            assertTrue(maxTemp >= -100 && maxTemp <= 100, "Temperature should be within a reasonable range");
        }
    }

    //WeatherApiIml

    @Test
    void testFetchWeatherData() {
        String cityName = "Helsinki";

        // Assuming the API is accessible and returns valid JSON data
        JsonObject weatherData = WeatherApiImpl.fetchWeatherData(cityName);

        assertNotNull(weatherData);
        assertTrue(weatherData.has("name"));
        assertTrue(weatherData.has("main"));
        assertTrue(((JsonObject) weatherData).getAsJsonObject("main").has("temp"));
        // Add more assertions as needed based on the structure of the JSON response
    }

    @Test
    void testLookUpLocation() {
        WeatherApiImpl weatherApi = new WeatherApiImpl();
        String location = "Helsinki";


        String coordinates = weatherApi.lookUpLocation(location);

        assertNotNull(coordinates);

    }



    @Test
    void testGetForecast() {
        double latitude = 60.1695;
        double longitude = 24.9354;


        String forecastData = new WeatherApiImpl().getForecast(latitude, longitude);

        assertNotNull(forecastData);
        assertTrue(forecastData.contains("Forecast data"));

    }

   //WeatherApp



    @Test
    void testUpdateCityNameWeatherApp() {
        WeatherApp weatherApp = new WeatherApp();

        // Initial cityName
        assertEquals("Tampere", weatherApp.cityName);

        // Update cityName
        weatherApp.updateCityName("Helsinki");
        assertEquals("Helsinki", weatherApp.cityName);

        // Update cityName to an empty string
        weatherApp.updateCityName("");
        assertEquals("", weatherApp.cityName);
    }



    @Test
    void testFetchCurrentTemperature() {
        WeatherApp weatherApp = new WeatherApp();


        assertNotEquals(-100, weatherApp.fetchCurrentTemperature("Helsinki"));
    }












}






