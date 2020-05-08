package com.example.arspapp_ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class trapping2 extends AppCompatActivity {
    private Spinner spinner2;
    ImageButton trapping2_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trapping2);


            Spinner spinner2 = findViewById(R.id.spinner2);
            trapping2_btn = findViewById(R.id.trapping2_btn);

            final String[] data = getResources().getStringArray(R.array.trapping_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,data);
            spinner2.setAdapter(adapter);

    }
    public void onClick(View v) {
        Intent intent;
        if (v==trapping2_btn) {
            intent = new Intent(trapping2.this, mainmenu.class);
            startActivity(intent);
        }
    }
}
