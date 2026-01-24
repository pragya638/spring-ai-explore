package com.llm.explore_openai.dto.weather;

public class WeatherResponse {
    private String city;
    private String description;
    private double temprature;
    private double humidity;
    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public double getTemprature() {
        return temprature;
    }

    public double getHumidity() {
        return humidity;
    }
    public void setCity(String city){
        this.city=city;
    }
    public void setDescription(String description){
        this.description=description;
    }
    public void setTemprature(double temprature){
        this.temprature=temprature;
    }
    public void setHumidity(double humidity){
        this.humidity=humidity;
    }
}
