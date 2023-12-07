package fi.tuni.prog3.weatherapp.Model;

/**
 * Represents a daily weather item with information about the date, weather icon, temperature range, humidity, and sea level.
 */
public class ModelDailyWeatherItem {

    /**
     * The date for which the weather information is applicable.
     */
    private String Date;

    /**
     * The icon representing the weather conditions.
     */
    private String weatherIcon;

    /**
     * The minimum temperature for the day.
     */
    private double minTemp;

    /**
     * The maximum temperature for the day.
     */
    private double maxTemp;

    /**
     * The humidity percentage for the day.
     */
    private int humidity;

    /**
     * The sea level for the day.
     */
    private double sealevel;

    /**
     * Constructs a new ModelDailyWeatherItem with default values.
     */
    public ModelDailyWeatherItem() {
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
     * Gets the minimum temperature for the day.
     *
     * @return The minimum temperature for the day.
     */
    public double getMinTemp() {
        return minTemp;
    }

    /**
     * Sets the minimum temperature for the day.
     *
     * @param minTemp The minimum temperature for the day.
     */
    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    /**
     * Gets the maximum temperature for the day.
     *
     * @return The maximum temperature for the day.
     */
    public double getMaxTemp() {
        return maxTemp;
    }

    /**
     * Sets the maximum temperature for the day.
     *
     * @param maxTemp The maximum temperature for the day.
     */
    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    /**
     * Sets the humidity percentage for the day.
     *
     * @param humidity The humidity percentage for the day.
     */
    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    /**
     * Sets the sea level for the day.
     *
     * @param seaLevel The sea level for the day.
     */
    public void setSeaLevel(double seaLevel) {
        this.sealevel = seaLevel;
    }

    /**
     * Gets the humidity percentage for the day.
     *
     * @return The humidity percentage for the day.
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * Gets the sea level for the day.
     *
     * @return The sea level for the day.
     */
    public double getSealevel() {
        return sealevel;
    }
}