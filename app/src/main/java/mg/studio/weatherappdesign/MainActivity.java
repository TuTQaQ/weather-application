package mg.studio.weatherappdesign;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextView weekText;
    private TextView dateText;
    private TextView locationText;
    private TextView temperText;

    private Button button;

    private Animation operatingAnim;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAll();

        btnClick();

        //btnClick();
    }

    private void initAll(){
        //init View
        imageButton=(ImageButton)findViewById(R.id.refresh_button);
        weekText=(TextView)findViewById(R.id.weekText);
        dateText=(TextView)findViewById(R.id.tv_date);
        locationText=(TextView)findViewById(R.id.tv_location);
        temperText=(TextView)findViewById(R.id.temperature_of_the_day);
        button=(Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClick();
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnClick();
            }
        });



        //init Animation
        operatingAnim= AnimationUtils.loadAnimation(this,R.anim.tip);
        LinearInterpolator lin=new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        //init calendar
        calendar=Calendar.getInstance();
    }

    public void btnClick() {

        //judge network connection
        boolean isNet=isNetworkConnected(getApplicationContext());
        if (isNet==false){
            Toast.makeText(getApplicationContext(),"Please connect network",Toast.LENGTH_SHORT).show();
            return;
        }

        //get info
        new DownloadUpdate().execute();

        // start animation
        imageButton.startAnimation(operatingAnim);

    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            //get info by okhttp;
            String url = "https://free-api.heweather.com/s6/weather/now?" +
                    "location=chongqing"+"&" +
                    "key=71050b8753b046e5b4cc0b5f0f70dd71"+"&"+
                    "lang=en"+"&"+ "unit=m";

            OkHttpClient okHttpClient = new OkHttpClient();
            final Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();


                String result=response.body().string();
                return result;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed

            //Resolve json by Gson;
            Gson gson=new Gson();
            GsonData data=gson.fromJson(temperature,GsonData.class);
            String cityname=data.HeWeather6.get(0).basic.location;
            String temper=data.HeWeather6.get(0).now.tmp;


            locationText.setText(cityname);
            temperText.setText(temper);

            //end animation
            imageButton.clearAnimation();

            //update time
            updateCalender();
        }
    }



    private void updateCalender(){
        String year =Integer.toString(calendar.get(Calendar.YEAR));
        String month =Integer.toString(calendar.get(Calendar.MONTH)+1);
        String day =Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        int week=calendar.get(Calendar.DAY_OF_WEEK);

        String strWeek="";
        switch (week){
            case Calendar.SUNDAY:
                strWeek="SUNDAY";
                break;
            case Calendar.MONDAY:
                strWeek="MONDAY";
                break;
            case Calendar.TUESDAY:
                strWeek="TUESDAY";
                break;
            case Calendar.WEDNESDAY:
                strWeek="WEDNESDAY";
                break;
            case Calendar.THURSDAY:
                strWeek="THURSDAY";
                break;
            case Calendar.FRIDAY:
                strWeek="FRIDAY";
                break;
            case Calendar.SATURDAY:
                strWeek="SATURDAY";
                break;
            default:
                break;
        }
        dateText.setText(day+'/'+month+'/'+year);
        weekText.setText(strWeek);
    }

}
