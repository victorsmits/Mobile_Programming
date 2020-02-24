package com.example.servercalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.URI;

public class OptionActivity extends AppCompatActivity {

    TextView LastOpeView;
    TextView LastResView;

    EditText Url;

    String LastOpe;
    int LastRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        LastOpeView = (TextView) findViewById(R.id.LastOpe);
        LastResView = (TextView) findViewById(R.id.LatRes);
        Url = findViewById(R.id.Url);
        Bundle extra = getIntent().getExtras();

        if(extra != null){
            LastOpe = extra.getString("LastOpe");
            LastRes = extra.getInt("LastRes");

            System.out.println(LastOpe);


            LastOpeView.setText(String.valueOf(LastOpe));
            LastResView.setText(String.valueOf(LastRes));
        }

    }

    public void URISearch(View view){
        Uri url = Uri.parse(String.valueOf(Url.getText()));
        if(url != null){
            Intent i = new Intent(Intent.ACTION_VIEW,url);
            startActivity(i);
        }else{
            Toast.makeText(this, "Please enter a valid url",
                    Toast.LENGTH_LONG).show();
        }

    }

}
