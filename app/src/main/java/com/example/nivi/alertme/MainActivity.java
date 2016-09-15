package com.example.nivi.alertme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText source;
    EditText destination;
    Button submit;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();
        int login = sp.getInt("login" , 0 );
        if(login == 0) {
            setContentView(R.layout.activity_main);
            source = (EditText) findViewById(R.id.editText);
            destination = (EditText) findViewById(R.id.editText2);
            submit = (Button) findViewById(R.id.button);


            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    editor.putString("source", source.getText().toString());
                    editor.putString("destination", destination.getText().toString());
                    editor.putInt("login", 1);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), DetailsActivity.class));
                    finish();
                }
            });
        }else
        {
            startActivity(new Intent(getApplicationContext(), DetailsActivity.class));
        }
    }

    public SharedPreferences getSharedPreferences() {

        return sp;
    }
}
