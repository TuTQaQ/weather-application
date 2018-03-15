package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextView weekText;
    private TextView dateText;

    private Animation operatingAnim;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAll();
    }

    private void initAll(){
        //init View
        imageButton=(ImageButton)findViewById(R.id.refresh_button);
        weekText=(TextView)findViewById(R.id.weekText);
        dateText=(TextView)findViewById(R.id.tv_date);

        //init Animation
        operatingAnim= AnimationUtils.loadAnimation(this,R.anim.tip);
        LinearInterpolator lin=new LinearInterpolator();
        operatingAnim.setInterpolator(lin);

        //init calendar
        calendar=Calendar.getInstance();
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();

        // start animation
        imageButton.startAnimation(operatingAnim);

    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "http://mpianatra.com/Courses/info.txt";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);

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
