package com.example.project21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Button startButton;
    Date today, startDate;
    String[] todayStringArray;
    EditText dateEditText, timeEditText;
    ImageView calendarImageView, clockImageView, infoImageView;
    TextView infoTextView, projectLogo, dateTextView, timeTextView;
    boolean infoVisible = false;
    String motivatedArrayListString, depressedArrayListString, confusedArrayListString, goodTimeArrayListString, badTimeArrayListString;  // no need to make static .. sharedpref through accessing
    boolean isOpenFirstTime = true;
    static SharedPreferences sharedPreferences;  // to access the same in another activities
    ConstraintLayout constraintLayout;



    // called onclick from startButton
    public void showDialogueAlert(View view) {

        hideKeyBoard();

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you ready to start?")
                .setMessage("Have you read all info?")
                .setPositiveButton("Yes, Let's Go!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setDate();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                infoTextView.setAlpha(1);
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);


    }

    // called from setDate(Positive button of alert), onCreate(if isOpenFirstTime is false)

    public void launchAnotherActivity() {
        Intent intent = new Intent(getApplicationContext(), Calender.class);
        startActivity(intent);
        finish();
    }


    // called from +ve button in alertDialogue.
    public void setDate() {
        try {

            startDate = sdf.parse(dateEditText.getText().toString() + " " + timeEditText.getText().toString());

            if (startDate.getTime() > today.getTime()) {
                throw new Exception("You really gonna start that late? Start now!");
            } else if (TimeUnit.MILLISECONDS.toDays(today.getTime()-startDate.getTime()) > 21) {
                throw new Exception("21 days already passed for the date!");
            } else if(Integer.parseInt(timeEditText.getText().toString().split(":")[0]) > 23 || Integer.parseInt(timeEditText.getText().toString().split(":")[0]) < 0 ||
                    Integer.parseInt(timeEditText.getText().toString().split(":")[1]) > 59 || Integer.parseInt(timeEditText.getText().toString().split(":")[1]) < 0 ||
                    Integer.parseInt(timeEditText.getText().toString().split(":")[2]) > 59 || Integer.parseInt(timeEditText.getText().toString().split(":")[0]) < 0) {
                throw new Exception("Enter the correct Time!");
            }

            // save startdate to sharedpref.

            sharedPreferences.edit().putString("startDateString", sdf.format(startDate)).apply();
            sharedPreferences.edit().putBoolean("isOpenFirstTime", false).apply();

            launchAnotherActivity();


        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    // called from oncreate.
    public void setEditTexts() {
        todayStringArray = sdf.format(today).split(" ");
        dateEditText.setText(todayStringArray[0]);
        timeEditText.setText(todayStringArray[1]);


    }


    // hide the soft input keyboard on click on screen view
    public void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(startButton.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // hide the supportActionBar
        Objects.requireNonNull(getSupportActionBar()).hide();


        today = new Date();
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        sharedPreferences = this.getSharedPreferences("com.example.project21", Context.MODE_PRIVATE);
        calendarImageView = findViewById(R.id.calendarImageView);
        calendarImageView.setOnClickListener(this);
        clockImageView = findViewById(R.id.clockImageView);
        clockImageView.setOnClickListener(this);
        startButton = findViewById(R.id.startButton);
        infoImageView = findViewById(R.id.infoImageView);
        infoImageView.setOnClickListener(this);
        infoTextView = findViewById(R.id.infoTextView);
        projectLogo = findViewById(R.id.projectLogo);
        constraintLayout = findViewById(R.id.constraintLayoutMain);
        projectLogo.setOnClickListener(this);
        constraintLayout.setOnClickListener(this);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);


        isOpenFirstTime = sharedPreferences.getBoolean("isOpenFirstTime", true);

        if (isOpenFirstTime == false) {

            launchAnotherActivity();


        } else {



            // check for the boolean if false then launch next Activity. "30,40,20,60,70,60,80,90,100,90,100"  "100,90,110,80,70,80,40,30,20,30,10"   "50,60,40,30,60,70,90,10,0,20,10"

            //  "12,13,a:1,4,a:5,6,a:12,13,a:12,13,a:1,2,a:3,4,a:4,5,a:5,6,a:3,5,a:6,7,a:7,8,a:5,6,a:7,8,a:2,3,a:2,4,a:4,5,a:2,5,a:5,6,a:1,4,a:a:a"   17,a:18,a:19,a:17,a:20,a:22,a:23,a:0,a:15,a:17,a:18,a:19,a:21,a:a:a:a:a:23,a:0,a:18,a:19,a:20,a"







            // set default values.. to zero done anyways.
            motivatedArrayListString = "0,0,0,0,0,0,0,0,0,0,0";
            depressedArrayListString = "0,0,0,0,0,0,0,0,0,0,0";
            confusedArrayListString = "0,0,0,0,0,0,0,0,0,0,0";
            goodTimeArrayListString = "a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a";
            badTimeArrayListString =  "a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a:a";

            // save all to sharedpref


            sharedPreferences.edit().putString("motivatedArrayListString", motivatedArrayListString).apply();
            sharedPreferences.edit().putString("depressedArrayListString", depressedArrayListString).apply();
            sharedPreferences.edit().putString("confusedArrayListString", confusedArrayListString).apply();
            sharedPreferences.edit().putString("goodTimeArrayListString", goodTimeArrayListString).apply();
            sharedPreferences.edit().putString("badTimeArrayListString", badTimeArrayListString).apply();


            timeEditText.setOnKeyListener(this);

            projectLogo.animate().alpha(1f).setDuration(3000);

            dateTextView.animate().alpha(1f).setStartDelay(3000).setDuration(500);
            dateEditText.animate().alpha(1f).setStartDelay(3000).setDuration(500);
            calendarImageView.animate().alpha(1f).setStartDelay(3000).setDuration(500);

            timeTextView.animate().alpha(1f).setStartDelay(3100).setDuration(500);
            timeEditText.animate().alpha(1f).setStartDelay(3100).setDuration(500);
            clockImageView.animate().alpha(1f).setStartDelay(3100).setDuration(500);

            startButton.animate().alpha(1f).setStartDelay(3200).setDuration(500);
            infoImageView.animate().alpha(0.3f).setStartDelay(3200).setDuration(500);

            setEditTexts();


            infoImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // start the activity in the browser.
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://personalexcellence.co/blog/21-day-trial/"));
                    startActivity(browserIntent);
                    return false;
                }
            });


        }


    }

    @Override
    public void onClick(View view) {

        hideKeyBoard();

        if (view.getId() == R.id.calendarImageView) {
            showDatePickerDialogue();
        } else if (view.getId() == R.id.clockImageView) {
            showTimePickerDialogue();
        } else if (view.getId() == R.id.infoImageView) {
            if (infoVisible == false) {
                infoTextView.setAlpha(1f);
                infoVisible = true;
            } else {
                infoTextView.setAlpha(0);
                infoVisible = false;
            }
        }

    }

    public void showDatePickerDialogue() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {




                dateEditText.setText(Integer.toString(date) + "/" +Integer.toString(month+1) + "/" + Integer.toString(year));
            }
        }, today.getYear() + 1900, today.getMonth(), today.getDate());
        datePickerDialog.show();
    }




    public void showTimePickerDialogue() {
        Calendar c = Calendar.getInstance();
        int HOURS = c.get(Calendar.HOUR_OF_DAY);  // HOUR gives only 12 hr format hours and HOUR_OF_DAY gives the hours in 24 hrs format.
        int MINS = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hrs, int mins) {

                timeEditText.setText(hrs + ":" + mins + ":00");
            }
        }, HOURS, MINS, true);

        timePickerDialog.show();


    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            showDialogueAlert(startButton);
        }
        return false;
    }
}