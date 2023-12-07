module fi.tuni.progthree.weatherapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;


    requires com.google.gson;
    requires json.simple;
    requires org.json;


    opens fi.tuni.prog3.weatherapp to javafx.fxml;
    opens fi.tuni.prog3.weatherapp.Model;

    exports fi.tuni.prog3.weatherapp;


}
