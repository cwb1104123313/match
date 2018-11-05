package com.example.administrator.match.domain;

public class EnvironmentalBean {

    private int pm;

    private int co2;

    private int LightIntensity;

    private int humidity;

    private int temperature;

    private int Status;

    public EnvironmentalBean() {
    }

    public EnvironmentalBean(int pm, int co2, int lightIntensity, int humidity, int temperature, int status) {
        this.pm = pm;
        this.co2 = co2;
        LightIntensity = lightIntensity;
        this.humidity = humidity;
        this.temperature = temperature;
        Status = status;
    }


    public int getPm() {
        return pm;
    }

    public void setPm(int pm) {
        this.pm = pm;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getLightIntensity() {
        return LightIntensity;
    }

    public void setLightIntensity(int lightIntensity) {
        LightIntensity = lightIntensity;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
