package com.example.chris.plantapplication;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

public class fragment_chart1 extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static fragment_chart1 newInstance(int page, String title) {
        fragment_chart1 fragmentFirst = new fragment_chart1();
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
        View view = inflater.inflate(R.layout.fragment_fragment_chart1, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int slotNumber = preferences.getInt("SlotNumber", 0);

        Plant currentPlant = plantDataBase.getInstance().getPlantBySlot(slotNumber);
        createGraphChart(currentPlant.getDayGrowth(), view);

        return view;
    }

    public ArrayList<Float> partitionArrayList(ArrayList<Float> list, int partitionNumber) {
        //This function will go through the array list and partition the array list by the partition number
        ArrayList<Float> holder = new ArrayList<>();
        int index = 0;
        float averageValue = 0;
        for (float eachValue : list) {
            averageValue += eachValue;
            // Each value in the list
            if (index == partitionNumber) { //Instead, index on incremented values
                //Then we add the value to the array since it is at that multiple
                averageValue = averageValue / (index);
                holder.add(averageValue);
                averageValue = 0;
                index = 0;
            }

            index += 1;
        }
        return holder;
    }

    public void createGraphChart(ArrayList<Float> values, View view) {
        ArrayList<Float> partitionedArray = new ArrayList<>();
        String message = "";
        int values_size = values.size();
        if (values_size <= 168) {
            partitionedArray = values;

            String size = Integer.toString(partitionedArray.size());
            message = "Plant height over the last " + size + " hours";

        } else if (values_size > 168  && values_size <= 744) {
            // If our values are less than 168 hours, then we can divide the data up into days
            partitionedArray = partitionArrayList(values, 24);
            String size = Integer.toString(partitionedArray.size());
            message = "Plant height over the last " + size + " days";


        } else if (values_size > 744 && values_size <= 2976) {
            //Then we divide the data up into weeks
            partitionedArray = partitionArrayList(values, 24 * 7);
            String size = Integer.toString(partitionedArray.size());
            message = "Plant height over the last " + size + " weeks";

        } else if (values_size > 2976) {
            //Then we divide the data up into months
            partitionedArray = partitionArrayList(values, 24 * 7 * 4);
            String size = Integer.toString(partitionedArray.size());
            message = "Plant height over the last " + size + " months";

        }

        GraphView tvLabel = (GraphView) view.findViewById(R.id.chart);

        LineChart graph = (LineChart) view.findViewById(R.id.chart);
        List<Entry> entries = new ArrayList<Entry>();
/*
        float test[] = {0, 2, 3, 5, 6, 7, 8, 9, 10, 3, 6, 19, 20, 0};
*/
        for (int i = 0; i < partitionedArray.size(); i++) {
            float y = partitionedArray.get(i); //Set the values of the chart to the points of the dat chart
            entries.add(new Entry((float) i, y));
        }

        LineDataSet dataSet = new LineDataSet(entries, message); // add entries to dataset
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); //cubic
        dataSet.setDrawValues(false);

        dataSet.setDrawCircles(false);
        Drawable drawable = ContextCompat.getDrawable(view.getContext(), R.drawable.graph_fragment_1);
        dataSet.setFillDrawable(drawable);

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
                return String.format("%.1f cm", value);
            }
        });
/*
        graph.getXAxis().setDrawGridLines(true);
        graph.setDrawBorders(false);
*/
        graph.getDescription().setText("");
        graph.getLegend().setEnabled(true);
        graph.setExtraOffsets(7.5f, 0, 0, 0);
/*        graph.getXAxis().setDrawGridLines(false);
        graph.getXAxis().setDrawAxisLine(false);*/

        /*graph.getAxisRight().setDrawGridLines(false);
        graph.getAxisRight().setDrawAxisLine(false);

        graph.getAxisLeft().setDrawGridLines(false);
        graph.getAxisLeft().setDrawAxisLine(false);*/

        graph.invalidate(); // refresh
    }
}