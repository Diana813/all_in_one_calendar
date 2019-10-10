package com.example.android.flowercalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ColorSettings extends AppCompatActivity {


    private CardView red;
    private CardView blue;
    private CardView yellow;
    private CardView green;

    private int clickedOn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_settings);

        red = (CardView) findViewById(R.id.red);
        yellow = (CardView) findViewById(R.id.yellow);
        green = (CardView) findViewById(R.id.green);
        blue = (CardView) findViewById(R.id.blue);


        setRed();
        setYellow();
        setGreen();
        setBlue();
    }

    public void setRed() {

        red.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 1;
                Intent intent = new Intent(ColorSettings.this, MainActivity.class);
                intent.putExtra("colorSettings", clickedOn);
                startActivity(intent);
            }

        });


    }

    private void setYellow() {

        yellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 2;
                Intent intent = new Intent(ColorSettings.this, MainActivity.class);
                intent.putExtra("colorSettings", clickedOn);
                startActivity(intent);
            }
        });
    }

    public void setGreen() {

        green.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 3;
                Intent intent = new Intent(ColorSettings.this, MainActivity.class);
                intent.putExtra("colorSettings", clickedOn);
                startActivity(intent);
            }
        });
    }

    private void setBlue() {

        blue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 4;
                Intent intent = new Intent(ColorSettings.this, MainActivity.class);
                intent.putExtra("colorSettings", clickedOn);
                startActivity(intent);
            }
        });
    }

}

