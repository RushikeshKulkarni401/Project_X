package com.example.project21;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Calender extends AppCompatActivity implements View.OnClickListener {

    Date today, startDate;
    String startDateString;
    long days;
    String daysString;
    TextView daysTextView, dayNumberTextView;
    ConstraintLayout constraintLayout;
    String motivatedArrayListString, depressedArrayListString, confusedArrayListString, goodTimeArrayListString, badTimeArrayListString;
    String[] openedFlags = new String[11];
    TextView resultTextView, questionTextView, motivatedTextView, depressedTextView, confusedTextView;
    ImageView motivationImageView, depressedImageView, confusedImageView;

    public void setUI() {

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (days < 10) {
            // ui = red
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.background_red));
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_red)));
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_red));
        } else if(days < 17) {
            // ui = yellow
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.background_yellow));
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_yellow)));
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_yellow));
        } else {
            // ui = green;
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.background_green));
            Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.action_bar_green)));
            window.setStatusBarColor(getResources().getColor(R.color.status_bar_yellow));
        }

        setIcons();

    }

    // called from setUI
    public void setIcons() {
        Random random = new Random();
        int n;
        n  = random.nextInt(3);
        String uri1 = "@drawable/" + "motivated" + n;
        n = random.nextInt(3);
        String uri2 = "@drawable/" + "depressed" + n;
        n = random.nextInt(3);
        String uri3 = "@drawable/" + "confusion" + n;

        int imageResource1 = getResources().getIdentifier(uri1, null, getPackageName());
        int imageResource2 = getResources().getIdentifier(uri2, null, getPackageName());
        int imageResource3 = getResources().getIdentifier(uri3, null, getPackageName());

        Drawable res1 = getResources().getDrawable(imageResource1);
        Drawable res2 = getResources().getDrawable(imageResource2);
        Drawable res3 = getResources().getDrawable(imageResource3);

        motivationImageView.setImageDrawable(res1);
        depressedImageView.setImageDrawable(res2);
        confusedImageView.setImageDrawable(res3);
    }


    // called from oncreate
    public void calculateDifference() {
        long diff = today.getTime() - startDate.getTime();
        days = TimeUnit.MILLISECONDS.toDays(diff);
        // set the dayString to the textView

        if (days < 10) {
            daysString =  "0" + Long.toString(days);
        } else {
            daysString = Long.toString(days);
        }

        dayNumberTextView.setText(daysString);


        dayNumberTextView.setY(-500);
        daysTextView.animate().alpha(1f).setDuration(3000);
        dayNumberTextView.animate().translationYBy(500).alpha(1.0f).setStartDelay(2000).setDuration(2000);

        // setUi
        setUI();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        daysTextView = findViewById(R.id.daysTextView);
        dayNumberTextView = findViewById(R.id.dayNumberTextView);
        constraintLayout = findViewById(R.id.constraintLayout);
        resultTextView = findViewById(R.id.resultTextView);
        motivationImageView = findViewById(R.id.motivatedImageView);
        depressedImageView = findViewById(R.id.depressedImageView);
        confusedImageView = findViewById(R.id.confusedImageView);
        questionTextView = findViewById(R.id.questionTextView);
        motivatedTextView = findViewById(R.id.motivateTextView);
        depressedTextView = findViewById(R.id.depressedTextView);
        confusedTextView = findViewById(R.id.confusedTextView);

        MainActivity.sharedPreferences = getApplicationContext().getSharedPreferences("com.example.project21", Context.MODE_PRIVATE);

        startDateString = MainActivity.sharedPreferences.getString("startDateString", "");

        motivatedArrayListString = MainActivity.sharedPreferences.getString("motivatedArrayListString", "NullString");
        depressedArrayListString = MainActivity.sharedPreferences.getString("depressedArrayListString", "NullString");
        confusedArrayListString = MainActivity.sharedPreferences.getString("confusedArrayListString", "NullString");

        goodTimeArrayListString = MainActivity.sharedPreferences.getString("goodTimeArrayListString", "NullString");

        badTimeArrayListString = MainActivity.sharedPreferences.getString("badTimeArrayListString", "Null String");




        getSupportActionBar().setTitle("Calendar");



        if (days > 21) {
            days = 21;
            Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
            intent.putExtra("days", days);
            startActivity(intent);
            finish();
        }




        try {

            today = new Date();
            startDate = MainActivity.sdf.parse(startDateString);   // must be surrounded with the try/catch block.

        } catch (Exception e) {
            e.printStackTrace();
        }

        calculateDifference();


        // fine for 21 days data..

        if (days >= 10) {
            resultTextView.setVisibility(View.VISIBLE);
        }

        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (days >= 10) {
                    Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
                    intent.putExtra("days", days);
                    startActivity(intent);
                }
            }
        });


        motivationImageView.setOnClickListener(this);
        depressedImageView.setOnClickListener(this);
        confusedImageView.setOnClickListener(this);


        animateQuestion();


    }

    // called from onClick of imageViews

    public void updateGoodTime() {

        String[] weekTimes = goodTimeArrayListString.split(":");


        String dayTime = weekTimes[(int)days];

        Calendar c = Calendar.getInstance();

        int hr = c.get(Calendar.HOUR_OF_DAY);  // 24 hr

        int min = c.get(Calendar.MINUTE);

        if (min >= 30) {
            if (hr == 23) {
                hr = 0;
            } else {
                hr = hr + 1;
            }
        }

        dayTime = dayTime.substring(0, dayTime.length()-1) + hr + ",a";

        weekTimes[(int)days] = dayTime;

        goodTimeArrayListString = "";

        for (int i=0; i < 22; i++) {
            goodTimeArrayListString +=  weekTimes[i];
            if (i != 21) {
                goodTimeArrayListString += ":";
            }
        }

        // goodTimearrayListString saved.

        MainActivity.sharedPreferences.edit().putString("goodTimeArrayListString", goodTimeArrayListString).apply();


    }

    // called from onclick of imageViews

    public void updateBadTime() {

        String[] weekTimes = badTimeArrayListString.split(":");

        String dayTime = weekTimes[(int)days];

        Calendar c = Calendar.getInstance();

        int hr = c.get(Calendar.HOUR_OF_DAY);  // 24 hr

        int min = c.get(Calendar.MINUTE);

        if (min >= 30) {
            if (hr == 23) {
                hr = 0;
            } else {
                hr = hr + 1;
            }
        }


        dayTime = dayTime.substring(0, dayTime.length()-1) + hr + ",a";

        weekTimes[(int)days] = dayTime;

        badTimeArrayListString = "";

        for (int i=0; i < 22; i++) {
            badTimeArrayListString +=  weekTimes[i];
            if (i != 21) {
                badTimeArrayListString += ":";
            }
        }

        // badTimeArrayListString saved.
        MainActivity.sharedPreferences.edit().putString("badTimeArrayListString", badTimeArrayListString).apply();

    }


    // called from oncreate.
    public void animateQuestion() {
        motivationImageView.setX(-500);
        confusedImageView.setX(500);
        questionTextView.animate().alpha(1).setStartDelay(4500).setDuration(1000);

        motivationImageView.animate().translationXBy(500).alpha(1).setDuration(1000).setStartDelay(4500);
        motivatedTextView.animate().alpha(1).setDuration(1000).setStartDelay(4500);

        depressedImageView.animate().alpha(1).setStartDelay(4500).setDuration(1000);
        depressedTextView.animate().alpha(1).setStartDelay(4500).setDuration(1000);

        confusedImageView.animate().translationXBy(-500).alpha(1).setDuration(1000).setStartDelay(4500);
        confusedTextView.animate().alpha(1).setDuration(1000).setStartDelay(4500);
    }


    public String updateArrayListStrings(@NonNull String objectStringArrayList) {

        openedFlags = objectStringArrayList.split(",");

        openedFlags[(int)(days/2)] = String.valueOf(Integer.parseInt( openedFlags[(int)(days/2)]) + 10);

        objectStringArrayList = "";

        for(int i = 0; i < 11; i++) {
            objectStringArrayList += openedFlags[i];
            if (i != 10) objectStringArrayList += ",";
        }

        return objectStringArrayList;
    }

    @Override
    public void onClick(View view) {

        int timerA = 0, timerB = 0, timerC = 0;

        if (view.getId() == R.id.motivatedImageView) {

            // increment the motivated
            motivatedArrayListString = updateArrayListStrings(motivatedArrayListString);
            //data saved
            MainActivity.sharedPreferences.edit().putString("motivatedArrayListString", motivatedArrayListString).apply();
            updateGoodTime();

            timerA += 700;

        } else if (view.getId() == R.id.depressedImageView) {

            // increment the depressed
           depressedArrayListString = updateArrayListStrings(depressedArrayListString);
           // data saved.
            MainActivity.sharedPreferences.edit().putString("depressedArrayListString", depressedArrayListString).apply();
            updateBadTime();

           timerB += 700;


        } else if (view.getId() == R.id.confusedImageView) {

            // increment the confused
            confusedArrayListString = updateArrayListStrings(confusedArrayListString);
            MainActivity.sharedPreferences.edit().putString("confusedArrayListString", confusedArrayListString).apply();
            updateBadTime();

            timerC += 700;
        }

        questionTextView.animate().alpha(0).setDuration(700).setStartDelay(0);
        motivationImageView.animate().alpha(0).setDuration(timerA).setStartDelay(0);
        motivatedTextView.animate().alpha(0).setDuration(timerA).setStartDelay(0);
        depressedImageView.animate().alpha(0).setDuration(timerB).setStartDelay(0);
        depressedTextView.animate().alpha(0).setDuration(timerB).setStartDelay(0);
        confusedImageView.animate().alpha(0).setDuration(timerC).setStartDelay(0);
        confusedTextView.animate().alpha(0).setDuration(timerC).setStartDelay(0);

    }
}