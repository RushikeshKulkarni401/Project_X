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
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;


public class TimesActivity extends AppCompatActivity {


    LineChart mChart;
    String goodTimeArrayListString, badTimeArrayListString;
    ArrayList<Entry> goodTimeValues, badTimeValues;
    int[] freqGoodTime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] freqBadTime = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    TextView goodTimesTextView, badTimesTextView;
    Button resetButton;
    Long days;




    // called from the onCreate. to find the individual frequency and upload the graph

    public void uploadTimesToGraph() {

        String[] goodTimeWeekArray = goodTimeArrayListString.split(":");


        for (int i = 0; i < 22; i++) {

            if (goodTimeWeekArray[i].length() > 1) {

                String[] goodTimeDayArray = goodTimeWeekArray[i].split(",");



                for (int j=0; j < goodTimeDayArray.length-1; j++) {

                    goodTimeValues.add(new Entry(i, Integer.parseInt(goodTimeDayArray[j])));

                    freqGoodTime[Integer.parseInt(goodTimeDayArray[j])] += 1;



                }

            }

        }


        String[] badTimeWeekArray = badTimeArrayListString.split(":");

        for (int i = 0; i < 22; i++) {



            if (badTimeWeekArray[i].length() > 1) {

                String[] badTimeDayArray = badTimeWeekArray[i].split(",");



                for (int j = 0; j < badTimeDayArray.length - 1; j++) {

                    badTimeValues.add(new Entry(i, Integer.parseInt(badTimeDayArray[j])));

                    freqBadTime[Integer.parseInt(badTimeDayArray[j])] += 1;


                }
            }

        }

    }



    // called from getmaxgoodandbadTimes
    public int modifiedSearch(ArrayList<Integer> al, ArrayList<Integer> tl) {
        int max = 0, maxIndex = -1;
        for (int i=0; i < al.size(); i++) {
            // here equal to means we prefer daytime
            if (al.get(i) > max) {
                if (!tl.contains(i)) {
                    max = al.get(i);
                    maxIndex = i;
                }
            }
        }
        return maxIndex;
    }



    // find the max freq called from oncreate
    public void getMaxGoodAndBadTimes() {



        int[] arr1 = freqGoodTime;
        int[] arr2 = freqBadTime;



        ArrayList<Integer> targetList1 = new ArrayList<Integer>();
        ArrayList<Integer> targetList2 = new ArrayList<Integer>();




        ArrayList<Integer> al1 = new ArrayList<Integer>();
        ArrayList<Integer> al2 = new ArrayList<Integer>();



        // add the elements to the arraylists
        for(int i:arr1) al1.add(i);
        for(int i:arr2) al2.add(i);




        int count = 1;
        int maxIt = 1;

        while(count <= 3 && maxIt++ <= al1.size()) {




            int max1 = modifiedSearch(al1, targetList1);  // modified search
            int max2 = modifiedSearch(al2, targetList2);  // modified search




            // check if max1 exists in targetList2

            if (targetList2.contains(max1)) {

                // dont add max1 to targetList1.. remove if from targetList2
                targetList2.remove(targetList2.indexOf(max1));


            } else {

                targetList1.add(max1);


            }

            if (targetList1.contains(max2)) {

                // dont add max2 to targetList2.. remove if from targetList1
                targetList1.remove(targetList1.indexOf(max2));
                count--;

            } else {

                targetList2.add(max2);
                count++;
            }


        }



        // targetLists readyl.


        String gg1 = "";

        if (targetList1.size() > 0) {

            for(int i=0; i < targetList1.size(); i++) {
                if (targetList1.get(i) != -1) {
                    gg1 += targetList1.get(i) + ":00  ";
                }
            }
            if (!(gg1.equals(""))) {
                goodTimesTextView.setText("Recommended Time To Work: " + gg1);
            }
        }

        String gg2 = "";

        if (targetList2.size() > 0) {

            for(int i=0; i < targetList2.size(); i++) {
                if (targetList2.get(i) != -1) {
                    gg2 += targetList2.get(i) + ":00  ";
                }
            }
            if (!(gg2.equals(""))) {
                badTimesTextView.setText("Recommended Time to Relax: " + gg2);
            }
        }

        if (gg1.equals("") && gg2.equals("")) {
            badTimesTextView.setText("The graph shows Good and bad times to work");
        }

    }

    // called from onclick from button
    public void resetData(View view) {
        // reset the boolean
        // reset the arrayListString
        // look for all visible/insvisible contents

        MainActivity.sharedPreferences.edit().putBoolean("isOpenFirstTime", true).apply();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_times);

        Intent intent = getIntent();
        days = intent.getLongExtra("days", 0);

        getSupportActionBar().setTitle("Time Management");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        goodTimesTextView = findViewById(R.id.goodTimesTextView);
        badTimesTextView = findViewById(R.id.badTimesTextView);
        resetButton = findViewById(R.id.resetButton);

        goodTimeValues = new ArrayList<>();
        badTimeValues = new ArrayList<>();

        MainActivity.sharedPreferences = getApplicationContext().getSharedPreferences("com.example.project21", Context.MODE_PRIVATE);

        goodTimeArrayListString = MainActivity.sharedPreferences.getString("goodTimeArrayListString", "NullString");
        badTimeArrayListString = MainActivity.sharedPreferences.getString("badTimeArrayListString", "NullString");



        if (days >= 21) {
            resetButton.setVisibility(View.VISIBLE);
            resetButton.setEnabled(true);
        }


        mChart = findViewById(R.id.lineChart1);

        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setScaleEnabled(true);
        mChart.setDragEnabled(true);
        mChart.getAxisRight().setDrawLabels(false);
        mChart.getAxisLeft().setAxisLineColor(Color.BLACK);
        mChart.getAxisLeft().setAxisLineWidth(2f);

        mChart.setBackgroundColor(Color.BLACK);   //
        mChart.getAxisLeft().setAxisLineColor(Color.WHITE);  //
        mChart.getXAxis().setAxisLineColor(Color.WHITE);  //
        mChart.getXAxis().setTextColor(Color.WHITE);   //
        mChart.getXAxis().setAxisLineWidth(2f); //
        mChart.getLegend().setTextColor(Color.WHITE);  //
        mChart.getAxisLeft().setTextColor(Color.WHITE); //


        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        mChart.getAxisLeft().setDrawGridLines(false);
//        mChart.getAxisRight().setDrawGridLines(false);
//        mChart.getXAxis().setDrawGridLines(false);

        mChart.getDescription().setText("Time-flow");

        mChart.getXAxis().setAxisMinimum(0);
        mChart.getXAxis().setAxisMaximum(21);

        mChart.getAxisLeft().setAxisMinimum(0);
        mChart.getAxisLeft().setAxisMaximum(23);
        mChart.getAxisLeft().setStartAtZero(true);


        uploadTimesToGraph();



        LineDataSet goodTimeSet = new LineDataSet(goodTimeValues, "Good Times");
        LineDataSet badTimeSet = new LineDataSet(badTimeValues, "Bad Times");




        goodTimeSet.setColor(Color.GREEN);
        goodTimeSet.setLineWidth(2f);
        goodTimeSet.setValueTextSize(10f);
        goodTimeSet.setValueTextColor(Color.GREEN);




        badTimeSet.setColor(Color.RED);
        badTimeSet.setLineWidth(2f);
        badTimeSet.setValueTextSize(10f);
        badTimeSet.setValueTextColor(Color.RED);



        ArrayList<ILineDataSet> dataSets = new ArrayList<>();




        if(goodTimeValues.size() > 0) {

            dataSets.add(goodTimeSet);

        } if (badTimeValues.size() > 0) {

             dataSets.add(badTimeSet);

        }



        LineData data = new LineData(dataSets);

        mChart.setData(data);

        getMaxGoodAndBadTimes();


    }
}