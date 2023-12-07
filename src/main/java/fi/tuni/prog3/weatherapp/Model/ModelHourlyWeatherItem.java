package fi.tuni.prog3.weatherapp.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


/**
 * Represents an hourly weather item with information about the hour, date, weather conditions, temperature, humidity, wind speed, and date-time.
 */
public class ModelHourlyWeatherItem {

    /**
     * The hour for which the weather information is applicable.
     */
    private long Hour;

    /**
     * The date for which the weather information is applicable.
     */
    private String Date;

    /**
     * The icon representing the weather conditions.
     */
    private String weatherIcon;

    /**
     * The minimum temperature for the hour.
     */
    private double minTemp;

    /**
     * The maximum temperature for the hour.
     */
    private double maxTemp;

    /**
     * The humidity percentage for the hour.
     */
    private String humidity;

    /**
     * The wind speed for the hour.
     */
    private String windSpeed;

    /**
     * The date-time for which the weather information is applicable.
     */
    private LocalDateTime dateTime;

    /**
     * Constructs a new ModelHourlyWeatherItem with default values.
     */
    public ModelHourlyWeatherItem() {
    }

    /**
     * Gets the date-time for which the weather information is applicable.
     *
     * @return The date-time for which the weather information is applicable.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Sets the date-time for which the weather information is applicable.
     *
     * @param dateTime The date-time for which the weather information is applicable.
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * Gets the date for which the weather information is applicable.
     *
     * @return The date for which the weather information is applicable.
     */
    public String getDate() {
        return Date;
    }

    /**
     * Sets the date for which the weather information is applicable.
     *
     * @param date The date for which the weather information is applicable.
     */
    public void setDate(String date) {
        Date = date;
    }

    /**
     * Gets the hour for which the weather information is applicable.
     *
     * @return The hour for which the weather information is applicable.
     */
    public long getHour() {
        return Hour;
    }

    /**
     * Sets the hour for which the weather information is applicable.
     *
     * @param hour The hour for which the weather information is applicable.
     */
    public void setHour(long hour) {
        Hour = hour;
    }

    /**
     * Gets the weather icon representing the weather conditions.
     *
     * @return The weather icon representing the weather conditions.
     */
    public String getWeatherIcon() {
        return weatherIcon;
    }

    /**
     * Sets the weather icon representing the weather conditions.
     *
     * @param weatherIcon The weather icon representing the weather conditions.
     */
    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    /**
     * Gets the minimum temperature for the hour.
     *
     * @return The minimum temperature for the hour.
     */
    public double getMinTemp() {
        return minTemp;
    }

    /**
     * Sets the minimum temperature for the hour.
     *
     * @param minTemp The minimum temperature for the hour.
     */
    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    /**
     * Gets the maximum temperature for the hour.
     *
     * @return The maximum temperature for the hour.
     */
    public double getMaxTemp() {
        return maxTemp;
    }

    /**
     * Sets the maximum temperature for the hour.
     *
     * @param maxTemp The maximum temperature for the hour.
     */
    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    /**
     * Gets the humidity percentage for the hour.
     *
     * @return The humidity percentage for the hour.
     */
    public String getHumidity() {
        return humidity;
    }

    /**
     * Sets the humidity percentage for the hour.
     *
     * @param humidity The humidity percentage for the hour.
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     * Gets the wind speed for the hour.
     *
     * @return The wind speed for the hour.
     */
    public String getWindSpeed() {
        return windSpeed;
    }

    /**
     * Sets the wind speed for the hour.
     *
     * @param windSpeed The wind speed for the hour.
     */
    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o The object to compare with.
     * @return True if this object is equal to the other, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelHourlyWeatherItem that = (ModelHourlyWeatherItem) o;
        return Hour == that.Hour &&
                Double.compare(that.minTemp, minTemp) == 0 &&
                Double.compare(that.maxTemp, maxTemp) == 0 &&
                Objects.equals(Date, that.Date) &&
                Objects.equals(weatherIcon, that.weatherIcon) &&
                Objects.equals(humidity, that.humidity) &&
                Objects.equals(windSpeed, that.windSpeed) &&
                Objects.equals(dateTime, that.dateTime);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(Hour, Date, weatherIcon, minTemp, maxTemp, humidity, windSpeed, dateTime);
    }
}