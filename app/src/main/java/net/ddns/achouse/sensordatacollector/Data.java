package net.ddns.achouse.sensordatacollector;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {
    private String type;
    private String timestamp;
    private String value;

    public Data(String type, String timestamp, String value) {
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(timestamp);
        dest.writeString(value);
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        public Data createFromParcel (Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public Data(Parcel in) {
        type = in.readString();
        timestamp = in.readString();
        value = in.readString();
    }
}
