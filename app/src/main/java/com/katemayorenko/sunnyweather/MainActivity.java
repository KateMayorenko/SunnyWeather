package com.katemayorenko.sunnyweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityNames;
    TextView desriptionText;

public void weatherButton (View view){

      Log.i("cityName", cityNames.getText().toString());

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityNames.getWindowToken(), 0);

        try {
            String encodedCityName = URLEncoder.encode(cityNames.getText().toString(), "UTF-8");

            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + encodedCityName);


        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

        }

}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityNames = (EditText)findViewById(R.id.cityNames);
        desriptionText = (TextView)findViewById(R.id.descriptionText);

      
    }
    public class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result="";
            URL url;
            HttpURLConnection httpURLConnection = null;

            try {
                url = new URL(urls[0]);

                httpURLConnection = (HttpURLConnection)url.openConnection();

                InputStream in = httpURLConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while(data!=-1){

                    char current = (char)data;

                    result+= current;

                    data = reader.read();
                }
                return result;

               } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

            }


            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
               String message = "";

                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content", weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);

                for(int i = 0;i<arr.length();i++){

                    JSONObject jsonPart = arr.getJSONObject(i);

                    String main="";

                    String description="";

                    main = jsonPart.getString("main");

                    description = jsonPart.getString("description");

                    if (main != "" && description != "") {

                        message += main + ": " + description + "\r\n";

                    }

                }

                if (message != "") {

                    desriptionText.setText(message);

                } else {

                    Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

                }


            } catch (JSONException e) {
            
                Toast.makeText(getApplicationContext(),"There are no characters",Toast.LENGTH_LONG).show();
            }


        }
    }


}
