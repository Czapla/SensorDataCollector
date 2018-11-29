package net.ddns.achouse.sensordatacollector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class GraphFragment extends Fragment {
    public static GraphFragment newInstance() {
        GraphFragment fragment = new GraphFragment();
        return fragment;
    }

    private ArrayList<Data> dataList = new ArrayList<Data>();
    ChartHandler mChartT;
    ChartHandler mChartH;
    ChartHandler mChartP;
    LineChart chartTemperature;
    LineChart chartHumidity;
    LineChart chartPressure;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graph_fragment, container, false);
    }

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void addDataPoint(float value, String type) {
        if(mChartT != null && mChartH != null && mChartP != null) {
            if(type.equals("Temperature")) {
                mChartT.addEntry(value);
            } else if(type.equals("Humidity")) {
                mChartH.addEntry(value);
            } else if(type.equals("Pressure")) {
                mChartP.addEntry(value);
            }
            //mChart.addEntry(value);
            Log.w("Debug", "Added Entry");
        }
    }
}
