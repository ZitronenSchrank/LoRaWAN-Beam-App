package dev.zitronenschrank.uplink;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import javax.annotation.Nullable;

import org.apache.beam.sdk.schemas.JavaBeanSchema;
import org.apache.beam.sdk.schemas.JavaFieldSchema;
import org.apache.beam.sdk.schemas.annotations.DefaultSchema;
import org.apache.beam.sdk.schemas.annotations.SchemaCreate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

public class UplinkMessage implements Serializable {

    private String deduplicationId;
    private String time;

    private String devAddr;
    private boolean adr;
    private int dr;
    private int fCnt;
    private int fPort;
    private boolean confirmed;
    private String data;

    @JsonProperty("object")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Nullable
    private SensorData object;

    public String getDeduplicationId() {
        return deduplicationId;
    }

    public String getTime() {
        return time;
    }

    public String getDevAddr() {
        return devAddr;
    }

    public boolean isAdr() {
        return adr;
    }

    public int getDr() {
        return dr;
    }

    public int getfCnt() {
        return fCnt;
    }

    public int getfPort() {
        return fPort;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getData() {
        return data;
    }

    public SensorData getObject() {
        return object;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((deduplicationId == null) ? 0 : deduplicationId.hashCode());
        result = prime * result + ((time == null) ? 0 : time.hashCode());
        result = prime * result + ((devAddr == null) ? 0 : devAddr.hashCode());
        result = prime * result + (adr ? 1231 : 1237);
        result = prime * result + dr;
        result = prime * result + fCnt;
        result = prime * result + fPort;
        result = prime * result + (confirmed ? 1231 : 1237);
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((object == null) ? 0 : object.hashCode());
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
        UplinkMessage other = (UplinkMessage) obj;
        if (deduplicationId == null) {
            if (other.deduplicationId != null)
                return false;
        } else if (!deduplicationId.equals(other.deduplicationId))
            return false;
        if (time == null) {
            if (other.time != null)
                return false;
        } else if (!time.equals(other.time))
            return false;
        if (devAddr == null) {
            if (other.devAddr != null)
                return false;
        } else if (!devAddr.equals(other.devAddr))
            return false;
        if (adr != other.adr)
            return false;
        if (dr != other.dr)
            return false;
        if (fCnt != other.fCnt)
            return false;
        if (fPort != other.fPort)
            return false;
        if (confirmed != other.confirmed)
            return false;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        if (object == null) {
            if (other.object != null)
                return false;
        } else if (!object.equals(other.object))
            return false;
        return true;
    }

}
