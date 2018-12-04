package net.ddns.achouse.sensordatacollector;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Klasa reprezentująca dane sensoryczne z brokera. Implementująca interfejs Parcelable, aby móc
 * parsować obiekty tej klasy w ramach innego komponentu.
 */
public class Data implements Parcelable {
    /**
     * Typ danych, wielkości fizycznej: Temperature, Humidity, Pressure.
     */
    private String type;
    /**
     * Czas odebrania danych.
     */
    private String timestamp;
    /**
     * Wartość danych, wielkości fizycznej.
     */
    private String value;

    /**
     * Konstruktor klasy Data.
     *
     * @param type
     * @see Data#type
     * @param timestamp
     * @see Data#timestamp
     * @param value
     * @see Data#value
     * @return Obiekt klasy Data.
     */
    public Data(String type, String timestamp, String value) {
        this.type = type;
        this.timestamp = timestamp;
        this.value = value;
    }

    /**
     *
     * @return Zwraca pole type obiektu.
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return Zwraca pole timestamp obiektu.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @return Zwraca pole value obiektu.
     */
    public String getValue() {
        return value;
    }

    /**
     * Jedna z metod intefejsu Parcelable.
     * @return Jako, że nie potrzebujemy tej metody to zwraca po prostu 0.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Sprowadza dane pól obiektu klasy do poziomu obiektu Parcel'a.
     * @param dest Docelowy Parcel.
     * @param flags Dodatkowe flagi, które określają jak dane mają być zapisywane do Parcel'a.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(timestamp);
        dest.writeString(value);
    }

    /**
     * Obiekt statyczny implementujący interfejs Parcelable.Creator.
     */
    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        public Data createFromParcel (Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    /**
     * Przeciążony konstruktor, który tworzy obiekt klasy Data z Parcel'a.
     * @param in Obiekt Parcel'a, z którego odczytywane są dane.
     */
    public Data(Parcel in) {
        type = in.readString();
        timestamp = in.readString();
        value = in.readString();
    }
}
