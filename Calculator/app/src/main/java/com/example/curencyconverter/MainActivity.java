package com.example.curencyconverter;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
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
//        progressBar.setVisibility(View.INVISIBLE);

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

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String query = String.join("",this.operation);
        }

        this.resultView.setText(String.valueOf(new AsyncEqual().execute()));
        this.operation.clear();
        this.status = true;
    }

    public class AsyncEqual extends AsyncTask<String,Integer,Integer> {

        @Override
        protected Integer doInBackground(String... operation) {
            int term1 = Integer.valueOf(operation[0]);
            int term2 = Integer.valueOf(operation[2]);
            int result = 0;


            switch (operation[1]){
                case "+":result = term1 + term2; break;
                case "-":result = term1 - term2; break;
                case "*":result = term1 * term2; break;
                case "/":result = term1 / term2; break;
            }

            return result;

        }

        protected void onProgressUpdate(Integer... progress) {
            progressBar.setProgress(progress[0]);
        }


//        @Override
        protected void onPostExecute(Float resultValue) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

    }

}
