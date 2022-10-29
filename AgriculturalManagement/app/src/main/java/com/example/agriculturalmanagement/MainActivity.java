package com.example.agriculturalmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import java.util.List;
import java.lang.Integer;

public class MainActivity extends AppCompatActivity {

    //JDBC dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){

            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(
                    R.id.fragment_container_view, MenuBarFragment.class, null).commit();

            // question: avoid, overtake, bypass, make db control indirect??
        }

        setContentView(R.layout.activity_main);


    }
}