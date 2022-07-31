package com.example.project21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    String motivatedArrayListString, depressedArrayListString, confusedArrayListString;
    String[] arrayOfData = new String[11];
    ImageView infoView;
    TextView resultTextView;
    LineChart mChart;
    ArrayList<Entry> motivatedValues, depressedValues, confusedValues;
    ArrayList<ILineDataSet> dataSets;
    Long days;



    // cycle the arrayListString in order if the user wants to practice more days for it to work.
    // but the day count will rise but the graph will be changing ofc.



    // called from oncreate

    public void fillDataSet() {
        arrayOfData = motivatedArrayListString.split(",");
        for(int i=0; i < 11; i++) {
            if ((days)/2 >= i) {
                Log.i("Info", "Printed");
                float temp = Integer.parseInt(arrayOfData[i]);
                motivatedValues.add(new Entry(2 * i + 1, temp));
            }
        }

        arrayOfData = depressedArrayListString.split(",");
        for(int i=0; i < 11; i++) {
            if ((days/2) >= i) {
                Log.i("Info", "Printed");
                float temp = Integer.parseInt(arrayOfData[i]);
                depressedValues.add(new Entry(2*i+1, temp));
            }
        }

        arrayOfData = confusedArrayListString.split(",");
        for(int i=0; i < 11; i++) {
            if ((days/2) >= i) {
                Log.i("Info", "Printed");
                float temp = Integer.parseInt(arrayOfData[i]);
                confusedValues.add(new Entry(2 * i + 1, temp));
            }
        }


    }

    // called from onclick by the NEXT button
    public void showTimeGraph(View view) {
        Intent intent = new Intent(getApplicationContext(), TimesActivity.class);

        intent.putExtra("days", days);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        Intent intent = getIntent();
        days = intent.getLongExtra("days", 0);



        // set the status and actionbar colours.
        getSupportActionBar().setTitle("Overall Progress");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        infoView = findViewById(R.id.infoView);
        resultTextView = findViewById(R.id.resultTextView);

        MainActivity.sharedPreferences = getApplicationContext().getSharedPreferences("com.example.project21", Context.MODE_PRIVATE);

        motivatedArrayListString = MainActivity.sharedPreferences.getString("motivatedArrayListString", "NullString");
        depressedArrayListString = MainActivity.sharedPreferences.getString("depressedArrayListString", "NullString");
        confusedArrayListString = MainActivity.sharedPreferences.getString("confusedArrayListString", "NullString");


        motivatedValues = new ArrayList<>();
        depressedValues = new ArrayList<>();
        confusedValues = new ArrayList<>();
        dataSets = new ArrayList<>();


        infoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultTextView.setVisibility(View.VISIBLE);
            }
        });

        //  from here the mChart works start.


        mChart = (LineChart) findViewById(R.id.lineChart);

        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setScaleEnabled(true);
        mChart.setDragEnabled(true);
        mChart.getAxisRight().setDrawLabels(false);

        mChart.setBackgroundColor(Color.BLACK);   //
        mChart.getAxisLeft().setAxisLineColor(Color.WHITE);  //
        mChart.getXAxis().setAxisLineColor(Color.WHITE);  //
        mChart.getXAxis().setTextColor(Color.WHITE);   //
        mChart.getXAxis().setAxisLineWidth(2f); //
        mChart.getLegend().setTextColor(Color.WHITE);  //
        mChart.getAxisLeft().setTextColor(Color.WHITE); //

        mChart.getAxisLeft().setAxisLineWidth(2f);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // remove grids and uncomment.
//        mChart.getAxisLeft().setDrawGridLines(false);
//        mChart.getAxisRight().setDrawGridLines(false);
//        mChart.getXAxis().setDrawGridLines(false);


        mChart.getXAxis().setAxisMinimum(0);
        mChart.getXAxis().setAxisMaximum(21);

        mChart.getAxisLeft().setAxisMinimum(0);
        mChart.getAxisLeft().setStartAtZero(true);

        // set the data from sharedpref

        fillDataSet();

        LineDataSet motivatedSet = new LineDataSet(motivatedValues, "% Motivation");
        LineDataSet depressedSet = new LineDataSet(depressedValues, "% Depression");
        LineDataSet confusedSet = new LineDataSet(confusedValues, "% Confusion");


        motivatedSet.setColor(Color.GREEN);
        motivatedSet.setLineWidth(2f);
        motivatedSet.setValueTextSize(10f);
        motivatedSet.setValueTextColor(Color.GREEN);


        depressedSet.setColor(Color.RED);
        depressedSet.setLineWidth(2f);
        depressedSet.setValueTextSize(10f);
        depressedSet.setValueTextColor(Color.RED);


        confusedSet.setColor(Color.BLUE);
        confusedSet.setLineWidth(2f);
        confusedSet.setValueTextSize(10f);
        confusedSet.setValueTextColor(Color.BLUE);

        if (motivatedValues.size() > 0) {
            dataSets.add(motivatedSet);
        }
        if (depressedValues.size() > 0){
            dataSets.add(depressedSet);
        }
        if (confusedValues.size() > 0){
            dataSets.add(confusedSet);
        }


        LineData data = new LineData(dataSets);

        mChart.setData(data);

    }
}