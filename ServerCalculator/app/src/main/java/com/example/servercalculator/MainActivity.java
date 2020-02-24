package com.example.servercalculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView resultView;
    Boolean status = false;
    TextView opeView;
    List<String> operation = new ArrayList<String>();
    String LastOpe = "";
    Integer LastRes = 0;

    private ProgressBar progressBar;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = (TextView) findViewById(R.id.result);
        opeView = (TextView) findViewById(R.id.ope);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout_tags);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        Button equalsBtn = new Button(this);
        equalsBtn.setTag("=");
        equalsBtn.setText("=");
        equalsBtn.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,1));

        equalsBtn.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
                MainActivity.this.equals(v);
            }
        });

        layout.addView(equalsBtn);
        handler = new Handler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,OptionActivity.class);
            i.putExtra("LastOpe",LastOpe);
            i.putExtra("LastRes",LastRes);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void myClickHandler(View view) {
        if (this.status) {
            this.opeView.setText("");
            this.status = false;
        }
        System.out.println(view.getTag().toString());
        this.operation.add(view.getTag().toString());
        this.opeView.setText(String.format("%s%s", this.opeView.getText(), view.getTag().toString()));
    }

    public void equals(View view){
        List<String> ope = new ArrayList<String>(Arrays.asList("+",
                "-",
                "*",
                "-",
                "/"));

        if(this.opeView.length() < 3 || ope.contains(this.operation.get(2))){
            Toast.makeText(this, "Please enter a valid operation",
                    Toast.LENGTH_LONG).show();
            this.opeView.setText("");
            this.operation.clear();
            return;
        }

        String query = String.join("",this.operation);

        new AsyncEqual().execute(query);


    }


    // ASYNC TASK

    @SuppressLint("StaticFieldLeak")
    public class AsyncEqual extends AsyncTask<String,Integer,Integer> {

        @Override
        protected Integer doInBackground(String... s) {
            int result = 0;
            try (Socket socket = new Socket("10.0.2.2", 9877)){
                String ope = Arrays.toString(s);
                LastOpe = ope.substring(1,ope.length()-1);

                DataInputStream InputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream OutputStream = new DataOutputStream(socket.getOutputStream());

                try{
                    System.out.println(ope);
                    for(int i = 1; i < ope.length()-1; i ++){
                        System.out.println(ope.charAt(i));
                        OutputStream.writeChar(ope.charAt(i));
                    }
                    Thread.sleep(500);

                    result = InputStream.readInt();
                    LastRes = result;
                    Thread.sleep(500);

                } catch (InterruptedException e) {
                    System.out.println(e);
                }

            } catch (IOException e) {
                System.out.println(e);
            }
            return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Integer resultValue) {
            progressBar.setVisibility(View.INVISIBLE);
            resultView.setText(String.valueOf(resultValue));
            operation.clear();
            status = true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

    }

}
