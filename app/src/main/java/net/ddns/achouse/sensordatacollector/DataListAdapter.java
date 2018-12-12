package net.ddns.achouse.sensordatacollector;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Klasa implementująca BaseAdapter, który na podstawie Listy(ArrayList) automatycznie wstawia dane do
 * ListView.
 */
public class DataListAdapter extends BaseAdapter {
    /**
     * Lista obiektów klasy Data.
     */
    private ArrayList<Data> listData;
    /**
     *  Inflater, który tworzy instancje layoutu XML.
     */
    private LayoutInflater layoutInflater;

    /**
     * Konstruktor klasy DataListAdapter.
     * @param aContext Kontekst, z którym połączony będzie Inflater.
     * @param listData Lista obiektów klasy Data.
     */
    public DataListAdapter(Context aContext, ArrayList<Data> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    /**
     * @return Zwraca rozmiar listy obiektów klasy Data.
     */
    @Override
    public int getCount() {
        return listData.size();
    }

    /**
     * @param position Pozycja w liście.
     * @return Zwraca obiekt klasy Data z listy o podanym indeksie.
     */
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    /**
     *
     * @param position Pozycja w liście.
     * @return Zwraca indeks.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Metoda tworzy wiersz w ListView.
     * @param position Pozycja w liście.
     * @param v View, który może być wypełniany Adapterem.
     * @param vg GroupView nieużywany w tym wypadku.
     * @return Zwraca wiersz do ListView.
     */
    public View getView(int position, View v, ViewGroup vg) {
        ViewHolder holder;
        if (v == null) {
            v = layoutInflater.inflate(R.layout.table_row, null);
            holder = new ViewHolder();
            holder.uType = (TextView) v.findViewById(R.id.type);
            holder.uTimestamp = (TextView) v.findViewById(R.id.timestamp);
            holder.uValue = (TextView) v.findViewById(R.id.value);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.uType.setText(listData.get(position).getType());
        switch(listData.get(position).getType()) {
            case "Temperature":
                holder.uType.setTextColor(Color.parseColor("#ff0000"));
                holder.uTimestamp.setTextColor(Color.parseColor("#ff0000"));
                holder.uValue.setTextColor(Color.parseColor("#ff0000"));
                break;
            case "Humidity":
                holder.uType.setTextColor(Color.parseColor("#0000ff"));
                holder.uTimestamp.setTextColor(Color.parseColor("#0000ff"));
                holder.uValue.setTextColor(Color.parseColor("#0000ff"));
                break;
            case "Pressure":
                holder.uType.setTextColor(Color.parseColor("#00ff00"));
                holder.uTimestamp.setTextColor(Color.parseColor("#00ff00"));
                holder.uValue.setTextColor(Color.parseColor("#00ff00"));
                break;
        }
        holder.uTimestamp.setText(listData.get(position).getTimestamp());
        holder.uValue.setText(listData.get(position).getValue());
        return v;
    }

    /**
     * Klasa przechowująca referencje do wewnętrznych elementów wiersza.
     */
    static class ViewHolder {
        /**
         * Element wiersza przechowujący typ danych.
         */
        TextView uType;
        /**
         * Element wiersza przechowujący czas odebrania danych.
         */
        TextView uTimestamp;
        /**
         * Element wiersza przechowujący wartość danych.
         */
        TextView uValue;
    }
}
