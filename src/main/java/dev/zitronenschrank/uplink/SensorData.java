package dev.zitronenschrank.uplink;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

public class SensorData implements Serializable {
    @JsonProperty("TEMP")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Float temperature;

    @JsonProperty("VISIBLE_LIGHT")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Float visibleLight;

    @JsonProperty("VBAT")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Float battery;

    @JsonProperty("RHUM")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Float humidity;

    public Float getTemperature() {
        return temperature;
    }

    public Float getVisibleLight() {
        return visibleLight;
    }

    public Float getBattery() {
        return battery;
    }

    public Float getHumidity() {
        return humidity;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((temperature == null) ? 0 : temperature.hashCode());
        result = prime * result + ((visibleLight == null) ? 0 : visibleLight.hashCode());
        result = prime * result + ((battery == null) ? 0 : battery.hashCode());
        result = prime * result + ((humidity == null) ? 0 : humidity.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SensorData other = (SensorData) obj;
        if (temperature == null) {
            if (other.temperature != null)
                return false;
        } else if (!temperature.equals(other.temperature))
            return false;
        if (visibleLight == null) {
            if (other.visibleLight != null)
                return false;
        } else if (!visibleLight.equals(other.visibleLight))
            return false;
        if (battery == null) {
            if (other.battery != null)
                return false;
        } else if (!battery.equals(other.battery))
            return false;
        if (humidity == null) {
            if (other.humidity != null)
                return false;
        } else if (!humidity.equals(other.humidity))
            return false;
        return true;
    }

}