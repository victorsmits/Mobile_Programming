package com.example.async_caluclator;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView resultView;
    Boolean status = false;
    TextView opeView;
    List<String> operation = new ArrayList<String>();
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


    // HANDLER

    public void startProgress(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<String> ope = new ArrayList<String>(Arrays.asList("+",
                        "-",
                        "*",
                        "-",
                        "/"));

                if(opeView.length() < 3 || ope.contains(operation.get(2))){
                    opeView.setText("");
                    operation.clear();
                    return;
                }

                String query = String.join("",operation);

                final int resultValue = handlerEquals(operation);
                // simulate a slow network !
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // make the operation on the UI, thus updating progressbar
                        progressBar.setProgress(4);
                        resultView.setText(String.valueOf(resultValue));
                        operation.clear();
                        status = true;
                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setProgress(0);
            }
        };
        new Thread(runnable).start();

    }

    private int handlerEquals(List<String> ope) {

        int term1 = Integer.valueOf(ope.get(0));
        int term2 = Integer.valueOf(ope.get(2));
        int result = 0;

        switch (ope.get(1)){
            case "+":result = term1 + term2; break;
            case "-":result = term1 - term2; break;
            case "*":result = term1 * term2; break;
            case "/":result = term1 / term2; break;
        }

        return result;

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

    public class AsyncEqual extends AsyncTask<String,Integer,Integer> {

        @Override
        protected Integer doInBackground(String... s) {
            String ope = Arrays.toString(s);
            System.out.println("query : " + ope);

            int term1 = (int) ope.charAt(1);
            int term2 = (int) ope.charAt(3);
            int result = 0;

            switch (ope.charAt(2)){
                case '+':result = term1 + term2; break;
                case '-':result = term1 - term2; break;
                case '*':result = term1 * term2; break;
                case '/':result = term1 / term2; break;
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
