package com.basov.burnedcaloriescalculator.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BurnedCaloriesCalculatorActivity extends AppCompatActivity {

    private SharedPreferences savedValues;

    private Float caloriesBurned = 0f;
    private Float weight = 0f;
    private Float milesRan = 1f;
    private Float bmi = 0f;
    private Integer feet = 0;
    private Integer inches = 0;

    private EditText weightEditText;
    private TextView milesTextView;

    private SeekBar milesSeekBar;

    private TextView caloriesTextView;


    private Spinner heightSpinnerFeet;
    private Spinner heightSpinnerInches;

    private TextView bmiTextView;
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        weightEditText = (EditText) findViewById(R.id.weightEditText);
        milesTextView = (TextView) findViewById(R.id.milesTextView);
        milesSeekBar = (SeekBar) findViewById(R.id.milesSeekBar);
        caloriesTextView = (TextView) findViewById(R.id.caloriesTextView);
        heightSpinnerFeet = (Spinner) findViewById(R.id.heightSpinnerFeet);
        heightSpinnerInches = (Spinner) findViewById(R.id.heightSpinnerInches);
        bmiTextView = (TextView) findViewById(R.id.bmiTextView);
        nameEditText = (EditText) findViewById(R.id.nameEditText);

        weightEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                calculateAndDisplay();
                return false;
            }
        });

        milesSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                if (progress == 0) {
                    milesRan = 1f;
                } else {
                    milesRan = (float) progress;
                }

                milesTextView.setText(milesRan.toString() + "mi");
                calculateAndDisplay();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.numbers, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        heightSpinnerFeet.setAdapter(adapter);
        heightSpinnerInches.setAdapter(adapter);

        heightSpinnerFeet.setSelection(5);
        heightSpinnerInches.setSelection(6);

        heightSpinnerFeet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                feet = position;
                calculateAndDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        heightSpinnerInches.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inches = position;
                calculateAndDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onPause() {

        SharedPreferences.Editor editor = savedValues.edit();

        editor.putString("weightEditText", weightEditText.getText().toString());
        editor.putString("milesTextView", milesTextView.getText().toString());
        editor.putInt("milesSeekBar", milesSeekBar.getProgress());
        editor.putString("caloriesTextView", caloriesTextView.getText().toString());
        editor.putInt("heightSpinnerFeet", heightSpinnerFeet.getSelectedItemPosition());
        editor.putInt("heightSpinnerInches", heightSpinnerInches.getSelectedItemPosition());
        editor.putString("bmiTextView", bmiTextView.getText().toString());
        editor.putString("nameEditText", nameEditText.getText().toString());


        editor.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();


        weightEditText.setText(savedValues.getString("weightEditText", ""));
        milesTextView.setText(savedValues.getString("milesTextView", ""));
        milesSeekBar.setProgress(savedValues.getInt("milesSeekBar", 1));
        caloriesTextView.setText(savedValues.getString("caloriesTextView", ""));
        heightSpinnerFeet.setSelection(savedValues.getInt("heightSpinnerFeet", 5));
        heightSpinnerInches.setSelection(savedValues.getInt("heightSpinnerInches", 6));
        bmiTextView.setText(savedValues.getString("bmiTextView", ""));
        nameEditText.setText(savedValues.getString("nameEditText", ""));

        calculateAndDisplay();
    }


    private void calculateAndDisplay() {

        String weightString = weightEditText.getText().toString();

        if (weightString.equals("")) {
            weight = 0f;
        } else {
            weight = Float.parseFloat(weightString);
        }



        caloriesBurned = 0.75f * weight * milesRan;

        bmi = 0f;
        if(feet != 0 && inches != 0){
            bmi = (weight * 703) / ((12 * feet + inches) * (12 * feet + inches));
        }


        caloriesTextView.setText(caloriesBurned.toString());
        bmiTextView.setText(bmi.toString());

    }

}
