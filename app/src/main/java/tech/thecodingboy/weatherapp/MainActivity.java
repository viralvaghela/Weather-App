package tech.thecodingboy.weatherapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.androdocs.httprequest.HttpRequest;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    String CITY;
    String API = "8118ed6ee68db2debfaaa5a44c832918";
    Button btnSubmit;
    EditText etCity;
    TextView temprature,mintemp,maxtemp,txtpressure,txtsunrise,txtsunset,txtwind,txthumidity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {
            btnSubmit=(Button)findViewById(R.id.btn);
            etCity=(EditText)findViewById(R.id.city);

            temprature=(TextView)findViewById(R.id.txtvTemprature);
            mintemp=(TextView)findViewById(R.id.txtvMinTemp);
            maxtemp=(TextView)findViewById(R.id.txtvMaxTemp);
            txtpressure=(TextView)findViewById(R.id.txtvPre);
            txtsunrise=(TextView)findViewById(R.id.txtvSunrise);
            txtsunset=(TextView)findViewById(R.id.txtvSunset);
            txtwind=(TextView)findViewById(R.id.txtvWind);
            txthumidity=(TextView)findViewById(R.id.txtvHumidity);



            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CITY=etCity.getText().toString();
                    new weatherTask().execute();
                }
            });

        }
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);


                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                //set values to TextViews
                temprature.setText(temp);
                mintemp.setText(tempMin);
                maxtemp.setText(tempMax);
                txtpressure.setText(pressure);
                txtsunrise.setText(sunrise.toString());
                txtsunset.setText(sunset.toString());
                txthumidity.setText(humidity);
                txtwind.setText(windSpeed);



               // Toast.makeText(MainActivity.this, "tempurature:"+temp, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {

                Toast.makeText(MainActivity.this, "Error:" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
