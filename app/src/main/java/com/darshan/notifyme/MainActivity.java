package com.darshan.notifyme;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;

import java.util.Date;
import java.util.HashMap;

public class MainActivity extends Activity {

    private String[] plants = new String[]{
            "1 minute",
            "15 minutes",
            "30 minutes",
            "1 hour",
            "2 hours",
            "3 hours",
            "4 hours",
            "5 hours",
            "6 hours"
    };
    private Spinner spinner;
    private Button button;
    private EditText editText;
    private CleverTapAPI cleverTapAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        button = findViewById(R.id.button);
        editText = findViewById(R.id.editText);
        CleverTapAPI.setDebugLevel(3);
        try {
            cleverTapAPI = CleverTapAPI.getInstance(getApplicationContext());
            CleverTapAPI.createNotificationChannel(this,"notifyme","Notify Me Channel","Reminder Notifications",NotificationManager.IMPORTANCE_HIGH,true);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plants);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(spinnerArrayAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reminder = editText.getText().toString();
                String time = spinner.getSelectedItem().toString();
                int minutes = 0;
                Log.e("NOTIFYME","reminder - "+ reminder + " time - "+ time);
                editText.setText("");
                spinner.setAdapter(spinnerArrayAdapter);
                switch (time){
                    case "1 minute" :
                        minutes = 1;
                        break;
                    case "15 minutes" :
                        minutes = 15;
                        break;
                    case "30 minutes" :
                        minutes = 30;
                        break;
                    case "1 hour" :
                        minutes = 60;
                        break;
                    case "2 hours" :
                        minutes = 2*60;
                        break;
                    case "3 hours" :
                        minutes = 3*60;
                        break;
                    case "4 hours" :
                        minutes = 4*60;
                        break;
                    case "5 hours" :
                        minutes = 5*60;
                        break;
                    case "6 hours" :
                        minutes = 6*60;
                        break;

                }

                Date date = new Date(System.currentTimeMillis()+minutes*60*1000);
                HashMap<String, Object> reminderProps = new HashMap<String, Object>();
                reminderProps.put("Reminder Text", reminder);
                reminderProps.put("Reminder Time", date);
                cleverTapAPI.event.push("Reminder Raised", reminderProps);

            }
        });
    }

}
