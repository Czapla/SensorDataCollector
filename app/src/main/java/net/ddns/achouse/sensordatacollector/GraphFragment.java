package net.ddns.achouse.sensordatacollector;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;


/**
 * Klasa implementująca Fragment wyświetlający formularz z polami do wypełnienia
 * potrzebnymi do połączenia z brokerem.
 */
public class GraphFragment extends Fragment {
    /**
     * Statyczna metoda tworząca nową instancję Fragmentu.
     * @return Obiekt klasy GraphFragment.
     */
    public static GraphFragment newInstance() {
        GraphFragment fragment = new GraphFragment();
        return fragment;
    }

    /**
     * Lista obiektów klasy Data.
     */
    private ArrayList<Data> dataList = new ArrayList<Data>();
    /**
     * Handler dla wykresu temperatury.
     */
    ChartHandler mChartT;
    /**
     * Handler dla wykresu wilgotności.
     */
    ChartHandler mChartH;
    /**
     * Handler dla wykresu ciśnienia.
     */
    ChartHandler mChartP;
    /**
     * Wykres temperatury.
     */
    LineChart chartTemperature;
    /**
     * Wykres wilgotności.
     */
    LineChart chartHumidity;
    /**
     * Wykres ciśnienia.
     */
    LineChart chartPressure;

    /**
     * Metoda reprezentująca moment w cyklu życia Fragmentu, zaraz po jego utworzeniu.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Metoda reprezentująca moment w cyklu życia Fragmentu, zaraz po utworzeniu instancji widoku
     * w interfejsie użytkownika.
     * @param inflater Inflater, do którego zostanie dowiązany XML fragmentu.
     * @param container ViewGroup, który jest pojemnikiem na obiekty interfejsu.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     * @return zwraca inflater powiązany z XML fragmentu.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graph_fragment, container, false);
    }

    /**
     * Metoda reprezentująca moment w cyklu życia Fragmentu, zaraz po utworzeniu instancji
     * intefejsu użytkownika. Tutaj przypisywane są elementy intefejsu do pól klasy oraz przypisywane są instancje obiektów typu Handler dla wykresów.
     * Są tutaj również tworzone punkty na wykresach z użyciem danych dopisanych do listy od początku życia aplikacji,
     * do momentu wyświetlenia fragmentu na ekranie.
     * @param view Widok, który został utworzony w cyklu życia Fragmentu.
     * @param savedInstanceState Obiekt typu Bundle, który reprezentuje stan Fragmentu.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        dataList = ((MainActivity)getActivity()).dataList;
        chartTemperature = (LineChart) view.findViewById(R.id.chartTemperature);
        mChartT = new ChartHandler(chartTemperature, "Temperature", "#ff0000");
        chartHumidity = (LineChart) view.findViewById(R.id.chartHumidity);
        mChartH = new ChartHandler(chartHumidity, "Humidity", "#0000ff");
        chartPressure = (LineChart) view.findViewById(R.id.chartPressure);
        mChartP = new ChartHandler(chartPressure, "Pressure", "#00ff00");

        //get Data created from the beggining of app lifecycle
        for(int i = 0; i < dataList.size(); i++) {
            if(dataList.get(i).getType().equals("Temperature")) {
                mChartT.addEntry(Float.parseFloat(dataList.get(i).getValue()));
            } else if(dataList.get(i).getType().equals("Humidity")) {
                mChartH.addEntry(Float.parseFloat(dataList.get(i).getValue()));
            } else if(dataList.get(i).getType().equals("Pressure")) {
                mChartP.addEntry(Float.parseFloat(dataList.get(i).getValue()));
            }
        }
    }

    /**
     * Metoda, która w zależności od typu danych, dodaje punkt do odpowiedniego wykresu. Wywoływana
     * z poziomu MainActivity.
     * @param value Wartość danej.
     * @param type Typ wielkości fizycznej.
     */
    public void addDataPoint(float value, String type) {
        if(mChartT != null && mChartH != null && mChartP != null) {
            if(type.equals("Temperature")) {
                mChartT.addEntry(value);
            } else if(type.equals("Humidity")) {
                mChartH.addEntry(value);
            } else if(type.equals("Pressure")) {
                mChartP.addEntry(value);
            }
            Log.w("Debug", "Added Entry");
        }
    }
}
