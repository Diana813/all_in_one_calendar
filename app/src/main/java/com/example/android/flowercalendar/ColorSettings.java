package com.example.android.flowercalendar;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.flowercalendar.data.Contract;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.android.flowercalendar.data.Contract.ColorDataEntry.COLOR_NUMBER;

public class ColorSettings extends AppCompatActivity {


    private RelativeLayout red;
    private RelativeLayout blue;
    private RelativeLayout yellow;
    private RelativeLayout green;
    private Uri colorDataUri;
    private int clickedOn = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_settings);

        red = (RelativeLayout) findViewById(R.id.red);
        yellow = (RelativeLayout) findViewById(R.id.yellow);
        green = (RelativeLayout) findViewById(R.id.green);
        blue = (RelativeLayout) findViewById(R.id.blue);
        colorDataUri = Contract.ColorDataEntry.CONTENT_URI;

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
                //saveData();
            }

        });



    }

    private void setYellow() {

        yellow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickedOn = 2;
                //saveData();
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
                //saveData();
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
                //saveData();
                Intent intent = new Intent(ColorSettings.this, MainActivity.class);
                intent.putExtra("colorSettings", clickedOn);
                startActivity(intent);
            }
        });
    }

    private void saveData() {

        if (colorDataUri == null &&
                Integer.parseInt(String.valueOf(clickedOn)) == 0) {
            return;
        }

        final ContentValues values = new ContentValues();

        values.put(COLOR_NUMBER, clickedOn);

        if (colorDataUri == null) {

            Uri newUri = getContentResolver().insert(Contract.ColorDataEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.insert_data_error),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.data_saved),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(colorDataUri, values,
                    null, null);
            if (rowsAffected == 0) {

                Toast.makeText(this, getString(R.string.edit_data_error),
                        Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, getString(R.string.data_edited),
                        Toast.LENGTH_SHORT).show();
            }
        }

        Intent intent = new Intent(ColorSettings.this, MainActivity.class);

        startActivity(intent);

        finish();
    }
}




