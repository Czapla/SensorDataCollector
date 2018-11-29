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

public class ChartHandler implements OnChartValueSelectedListener {
    private LineChart mChart;
    private String dataLabel;
    private String color;
    //private ArrayList<String> Xlabels;
    //private ArrayList<Float> Yvalues;
    public ChartHandler(LineChart chart, String dataLabel, String color) {
        mChart = chart;
        mChart.setOnChartValueSelectedListener(this);

        this.dataLabel = dataLabel;
        this.color = color;

        // no description text
        mChart.setNoDataText("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.WHITE);
        //mChart.setBorderColor(Color.rgb(67,164,34));
        mChart.setBorderColor(Color.parseColor(color));
        mChart.getDescription().setEnabled(false);
        //Xlabels = new ArrayList<String>();
        //Yvalues = new ArrayList<Float>();
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(Typeface.MONOSPACE);
        l.setTextColor(Color.parseColor(color));

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(Typeface.MONOSPACE);
        xl.setTextColor(Color.parseColor(color));
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(false);
        //xl.setLabelRotationAngle(-90);
        /*xAxis.setValueFormatter(new DateValueFormatter());
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //Date timestamp = new Date();
                //DateFormat timestampFormatGraph = new SimpleDateFormat("hh:mm:ss", new Locale("pl", "PL"));
                //String strTimestampGraph = timestampFormatGraph.format(timestamp);
                for(int i = 0; i < Yvalues.size(); i++) {
                    if(Yvalues.get(i).equals(value)){
                        return Xlabels.get(i);
                    }
                }
                return "failed";
                //return  strTimestampGraph;
                //return new Date(new Float(value).longValue()).toString();
            }
        });
*/
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(Typeface.MONOSPACE);
        leftAxis.setTextColor(Color.parseColor(color));

        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);

    }

    public void setChart(LineChart chart){ this.mChart = chart; }

    public void addEntry(float value) {

        LineData data = mChart.getData();

        if (data != null){

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(),value),0);
            //Xlabels.add(timestamp);
            //Yvalues.add(value);
            Log.w("chart", set.getEntryForIndex(set.getEntryCount()-1).toString());

            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(10);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewTo(set.getEntryCount()-1, data.getYMax(), YAxis.AxisDependency.LEFT);

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, dataLabel);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.parseColor(color));
        //set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        //set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.parseColor(color));
        set.setHighLightColor(Color.parseColor(color));
        set.setValueTextColor(Color.parseColor(color));
        set.setValueTextSize(9f);
        set.setDrawValues(true);
        return set;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected(){
        Log.i("Nothing selected", "Nothing selected.");
    }

}
