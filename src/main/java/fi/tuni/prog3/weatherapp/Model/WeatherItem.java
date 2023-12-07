/**
 * The {@code WeatherItem} class represents a weather item containing information such as
 * days of the week, date of the month, weather icon, and temperature.
 * <p>
 * This class includes methods to retrieve and update the values of days of the week, date of the month,
 * weather icon, and temperature.
 * <p>
 * Instances of this class are typically used to store and display weather information for a specific day.
 *
 * @author Anisul Mahmud
 * @version 1.0
 * @since 2023-12-04
 */
package fi.tuni.prog3.weatherapp.Model;

import java.util.Objects;

/**
 * Represents a generic weather item with basic information.
 * This class can be extended or used as a base class for more specific weather-related objects.
 */
public class WeatherItem {
    /**
     * The days of the week for which the weather information is represented.
     */
    private String daysOfWeek;

    /**
     * The date of the month for which the weather information is represented.
     */
    private String dateOfMonth;

    /**
     * The weather icon representing the weather condition.
     */
    private String weatherIcon;

    /**
     * The temperature for the represented day.
     */
    private String temperature;

    /**
     * Constructs a new {@code WeatherItem} with the specified days of the week, date of the month,
     * weather icon, and temperature.
     *
     * @param daysOfWeek   the days of the week
     * @param dateOfMonth  the date of the month
     * @param weatherIcon  the weather icon representing the weather condition
     * @param temperature  the temperature for the represented day
     */
    public WeatherItem(String daysOfWeek, String dateOfMonth, String weatherIcon, String temperature) {
        this.daysOfWeek = daysOfWeek;
        this.dateOfMonth = dateOfMonth;
        this.weatherIcon = weatherIcon;
        this.temperature = temperature;
    }

    /**
     * Returns the days of the week.
     *
     * @return the days of the week
     */
    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    /**
     * Sets the days of the week.
     *
     * @param daysOfWeek the new days of the week
     */
    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /**
     * Returns the date of the month.
     *
     * @return the date of the month
     */
    public String getDateOfMonth() {
        return dateOfMonth;
    }

    /**
     * Sets the date of the month.
     *
     * @param dateOfMonth the new date of the month
     */
    public void setDateOfMonth(String dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
    }

    /**
     * Returns the weather icon.
     *
     * @return the weather icon
     */
    public String getWeatherIcon() {
        return weatherIcon;
    }

    /**
     * Sets the weather icon.
     *
     * @param weatherIcon the new weather icon
     */
    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    /**
     * Returns the temperature for the represented day.
     *
     * @return the temperature for the represented day
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * Sets the temperature for the represented day.
     *
     * @param temperature the new temperature for the represented day
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }


    /**
     * Compares this {@code WeatherItem} object to another object for equality.
     * Two {@code WeatherItem} objects are considered equal if they have the same
     * values for days of the week, date of the month, weather icon, and temperature.
     *
     * @param o the object to compare to
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherItem that = (WeatherItem) o;
        return Objects.equals(daysOfWeek, that.daysOfWeek) &&
                Objects.equals(dateOfMonth, that.dateOfMonth) &&
                Objects.equals(weatherIcon, that.weatherIcon) &&
                Objects.equals(temperature, that.temperature);
    }


    /**
     * Returns a hash code value for this {@code WeatherItem} object.
     * The hash code is based on the values of days of the week, date of the month,
     * weather icon, and temperature.
     *
     * @return the hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(daysOfWeek, dateOfMonth, weatherIcon, temperature);
    }

}
