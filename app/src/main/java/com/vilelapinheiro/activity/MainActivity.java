package com.vilelapinheiro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vilelapinheiro.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref =
                PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPref.getBoolean
                (SettingsActivity.KEY_NIGHT_MODE, false);
        Toast.makeText(this, switchPref.toString(), Toast.LENGTH_LONG).show();
    }

    public void openPatients(View view) {
        Intent about = new Intent(this, PatientsListActivity.class);
        startActivity(about);
    }

    public void openSettings(View view) {
        Intent about = new Intent(this, SettingsActivity.class);
        startActivity(about);
    }


}