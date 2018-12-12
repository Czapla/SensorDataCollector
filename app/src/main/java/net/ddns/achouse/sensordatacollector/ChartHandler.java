package net.ddns.achouse.sensordatacollector;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

/**
 * Klasa typu Handler do obsługi wykresów. Zdefiniowane tutaj pola i metody służą do utworzenia wykresów z
 * odpowiednimi parametrami oraz dodawania kolejnych punktów w serii danych.
 */
public class ChartHandler /*implements OnChartValueSelectedListener*/ {
    /**
     * Obiekt klasy LineChart reprezentujący wykres.
     */
    private LineChart mChart;
    /**
     * Nazwa serii danych.
     */
    private String dataLabel;
    /**
     * Kolor wykresu.
     */
    private String color;

    /**
     * Konstruktor klasy ChartHandler, w którym ustawiany jest Listener obsługujący zdarzenia kliknięcia
     * na dany punkt na wykresie oraz ustawiane są pozostałe parametry wykresu.
     * @param chart Obiekt klasy LineChart reprezentujący wykres.
     * @param dataLabel Nazwa serii danych.
     * @param color Kolor wykresu.
     */
    public ChartHandler(LineChart chart, String dataLabel, String color) {
        mChart = chart;
        //mChart.setOnChartValueSelectedListener(this);

        this.dataLabel = dataLabel;
        this.color = color;

        /**
         * Tekst wyświetlany, gdy jest brak danych do wyświetlenia.
         */
        mChart.setNoDataText("You need to provide data for the chart.");

        /**
         * Wyłączenie gestów dotykowych.
         */
        mChart.setTouchEnabled(false);

        /**
         * Wyłączenie przeciągania oraz skalowania
         */
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        //mChart.setPinchZoom(true);

        /**
         * Ustawienia koloru
         */
        mChart.setBackgroundColor(Color.WHITE);
        mChart.setBorderColor(Color.parseColor(color));
        mChart.getDescription().setEnabled(false);
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        /**
         * Dodanie pustych danych.
         */
        mChart.setData(data);

        /**
         * Dostanie się do legendy wykresu.
         */
        Legend l = mChart.getLegend();

        /**
         * Ustawienia legendy.
         */
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(Typeface.MONOSPACE);
        l.setTextColor(Color.parseColor(color));

        /**
         * Ustawienia dotyczące osi X.
         */
        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(Typeface.MONOSPACE);
        xl.setTextColor(Color.parseColor(color));
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(false);

        /**
         * Ustawienia dotyczące osi Y.
         */
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.MONOSPACE);
        leftAxis.setTextColor(Color.parseColor(color));
        leftAxis.setDrawGridLines(true);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    /**
     * Metoda dodaje punkt na wykresie o wartości Y równej przekazanej wartości danej.
     * @param value Wartość danej.
     */
    public void addEntry(float value) {

        LineData data = mChart.getData();

        if (data != null){

            ILineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(),value),0);
            Log.w("chart", set.getEntryForIndex(set.getEntryCount()-1).toString());

            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(10);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewTo(set.getEntryCount()-1, data.getYMax(), YAxis.AxisDependency.LEFT);
        }
    }

    /**
     * Metoda tworząca pustą serie danych z odpowiednimi parametrami.
     * @return Zwraca serię danych z odpowiednimi parametrami.
     */
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, dataLabel);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor(color));
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(Color.parseColor(color));
        set.setHighLightColor(Color.parseColor(color));
        set.setValueTextColor(Color.parseColor(color));
        set.setValueTextSize(9f);
        set.setDrawValues(true);
        return set;
    }

/*
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected(){
        Log.i("Nothing selected", "Nothing selected.");
    }
*/

}
