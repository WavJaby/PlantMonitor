package com.app.plantmonitor;

public class DataObject {
    public float lightSensor;
    public float temperature;
    public float humidity;
    public float soilMoisture;

    public DataObject(){
    };

    public DataObject(float lightSensor, float temperature, float humidity, float soilMoisture) {
        this.lightSensor = lightSensor;
        this.temperature = temperature;
        this.humidity = humidity;
        this.soilMoisture = soilMoisture;
    }

    public float getLightSensor() {
        return lightSensor;
    }

    public void setLightSensor(float lightSensor) {
        this.lightSensor = lightSensor;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(float soilMoisture) {
        this.soilMoisture = soilMoisture;
    }
}
