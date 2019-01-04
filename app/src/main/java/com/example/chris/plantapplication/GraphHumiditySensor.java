package com.example.chris.plantapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphHumiditySensor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphHumiditySensor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphHumiditySensor extends android.support.v4.app.Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static GraphHumiditySensor newInstance(int page, String title) {
        GraphHumiditySensor fragmentFirst = new GraphHumiditySensor();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_humidity_sensor, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int slotNumber = preferences.getInt("SlotNumber", 0);

        Plant currentPlant = plantDataBase.getInstance().getPlantBySlot(slotNumber);
        createGraphChart(currentPlant.getHumititySensor_harvestPeriod(), view);

        return view;
    }
    public void createGraphChart(float values[], View view){
        GraphView tvLabel = (GraphView) view.findViewById(R.id.chart1);

        LineChart graph = (LineChart) view.findViewById(R.id.chart1);
        List<Entry> entries = new ArrayList<Entry>();

        float test[] = {0, 2, 3, 5, 6, 7, 8, 9, 10, 3, 6, 19, 20, 0};

        for (int i = 0; i < test.length; i++) {
            float y = test[i]; //Set the values of the chart to the points of the dat chart
            entries.add(new Entry((float) i, y));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Soil humidity over elapsed days"); // add entries to dataset
        dataSet.setValueTextColor(Color.parseColor("#b906af49")); //Colour of text

        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); //cubic
        dataSet.setDrawValues(false);

        dataSet.setDrawCircles(false);
        Drawable drawable = ContextCompat.getDrawable(view.getContext(),R.drawable.graph_fragment_2);
        dataSet.setFillDrawable(drawable);
        dataSet.setColor(Color.parseColor("#b906af49"));
        //(getResources().getColor(R.color.gradient_plant_height));
        dataSet.setDrawFilled(true);
        //dataSet.setColor(getResources().getColor(R.color.gradient_plant_height));

        LineData lineData = new LineData(dataSet);

        dataSet.setFillAlpha(255);
        graph.setData(lineData);
        graph.setScaleEnabled(false);
        graph.setPinchZoom(false);
        graph.setDoubleTapToZoomEnabled(false);

        graph.setDrawGridBackground(false);
        graph.getAxisRight().setEnabled(false);
        graph.getLegend().setWordWrapEnabled(true);

        graph.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.format("%.1f%%", value);
            }
        });
    /*  graph.getXAxis().setDrawGridLines(true);
        graph.setDrawBorders(false);*/
        graph.getDescription().setText("");
        graph.getLegend().setEnabled(true);
        graph.setExtraOffsets(7.5f, 0, 0, 0);
        graph.invalidate(); // refresh
    }
}
