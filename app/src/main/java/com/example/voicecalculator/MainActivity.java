package com.example.voicecalculator;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.telecom.RemoteConference;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView firstNumTextView;
    TextView secondNumTextView;
    TextView operatorTextView;
    TextView resultTextView;
    Button goButton;

    TextToSpeech textToSpeech;

    private int FIRST_NUMBER;
    private int SECOND_NUMBER;
    private char OPERATOR;
    private int RESULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(this, this);

        firstNumTextView = findViewById(R.id.firstNumTextView);
        secondNumTextView = findViewById(R.id.secondNumTextView);
        operatorTextView = findViewById(R.id.operatorTextView);
        resultTextView = findViewById(R.id.resultTextView);
        goButton = findViewById(R.id.goButton);

        firstNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, 10);
            }
        });

        secondNumTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, 20);
            }
        });

        operatorTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                startActivityForResult(intent, 30);
            }
        });

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RESULT = performCalculations();
                resultTextView.setText(String.valueOf(RESULT));
                textToSpeech.speak(String.valueOf(RESULT), TextToSpeech.QUEUE_ADD, null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //
        if (resultCode == RESULT_OK && data != null) {

            switch (requestCode) {
                case 10:
                    int intFound = getIntNumberFromText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase());
                    if (intFound != -1) {
                        FIRST_NUMBER = intFound;
                        firstNumTextView.setText(String.valueOf(intFound));
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 20:
                    intFound = getIntNumberFromText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase());
                    if (intFound != -1) {
                        SECOND_NUMBER = intFound;
                        secondNumTextView.setText(String.valueOf(intFound));
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
                    break;
                case 30:
                    char operatorFound = getOperatorFromResult(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0).toLowerCase());
                    if (operatorFound != '0') {
                        OPERATOR = operatorFound;
                        operatorTextView.setText(String.valueOf(operatorFound).toLowerCase());
                    } else {
                        Toast.makeText(getApplicationContext(), "Sorry, I didn't catch that! Please try again", Toast.LENGTH_LONG).show();
                    }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }

    // method to loop through results trying to find a number
    private int getNumberFromResult(ArrayList<String> results) {
        for (String str : results) {
            if (getIntNumberFromText(str) != -1) {
                return getIntNumberFromText(str);
            }
        }
        return -1;
    }

    // method to loop through results trying to find an operator
    private char getOperatorFromResult(String str) {

            if (getCharOperatorFromText(str) != '0') {
                return getCharOperatorFromText(str);
            }

        return '0';
    }

    // method to convert string number to integer
    // manual labor soon
    private int getIntNumberFromText(String strNum) {
        switch (strNum) {
            case "zero":
            case "0":
                return 0;
            case "one":
            case "1":
                return 1;
            case "two":
            case "2":
                return 2;
            case "three":
            case "3":
                return 3;
            case "four":
            case "4":
                return 4;
            case "five":
            case "5":
                return 5;
            case "6":
            case "six":
                return 6;
            case "seven":
            case "7":
                return 7;
            case "eight":
            case "8":
                return 8;
            case "nine":
            case "9":
                return 9;
        }
        return -1;
    }

    // method to convert string operator to char
    private char getCharOperatorFromText(String strOper) {
        switch (strOper) {
            case "plus":
            case "add":
                return '+';
            case "minus":
            case "subtract":
                return '-';
            case "times":
            case "multiply":
                return '*';
            case "divided by":
            case "divide":
                return '/';
            case "power":
            case "raised to":
                return '^';
        }
        return '0';
    }

    private int performCalculations() {
        switch (OPERATOR) {
            case '+':
                return FIRST_NUMBER + SECOND_NUMBER;
            case '-':
                return FIRST_NUMBER - SECOND_NUMBER;
            case '*':
                return FIRST_NUMBER * SECOND_NUMBER;
            case '/':
                return FIRST_NUMBER / SECOND_NUMBER;
            case '^':
                return FIRST_NUMBER ^ SECOND_NUMBER;
        }
        return -999;
    }

    @Override
    public void onInit(int i) {

    }
}