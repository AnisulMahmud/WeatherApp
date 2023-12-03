package fi.tuni.prog3.weatherapp.Model;

public class WeatherItem {
    private String daysOfWeek;
    private String dateOfMonth;
    private String weatherIcon;
    private String temperature;

    public WeatherItem(String daysOfWeek, String dateOfMonth, String weatherIcon, String temperature) {
        this.daysOfWeek = daysOfWeek;
        this.dateOfMonth = dateOfMonth;
        this.weatherIcon = weatherIcon;
        this.temperature = temperature;
    }

    public String getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public String getDateOfMonth() {
        return dateOfMonth;
    }

    public void setDateOfMonth(String dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

}