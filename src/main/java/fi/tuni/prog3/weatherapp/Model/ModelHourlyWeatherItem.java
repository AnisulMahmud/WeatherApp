package fi.tuni.prog3.weatherapp.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ModelHourlyWeatherItem {

    private long Hour;
    private String Date;
    private String weatherIcon;
    private double minTemp;
    private double maxTemp;
    private String humidity;
    private String windSpeed;
    private LocalDateTime dateTime;

    public ModelHourlyWeatherItem() {
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public long getHour() {
        return Hour;
    }

    public void setHour(long Hour) {
        this.Hour = Hour;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
