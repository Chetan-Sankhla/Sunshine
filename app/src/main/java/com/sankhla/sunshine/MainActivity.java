package com.sankhla.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{
    private static final String url="http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid=";
    private static final String DETAIL_FRAG_TAG="DFTAG";
    private final String LOG_TAG=MainActivity.class.getSimpleName();

    private String mLocation;
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.v(LOG_TAG,"in onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container,new ForecastFragment()).commit();
        }
        //mLocation=
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();

        if (id==R.id.action_setting)
        {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        if (id==R.id.action_map)
        {
            openPreferredLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap()
    {
        String location=Utility.getPreferredLocation(this);

        Uri geoLocation=Uri.parse("geo:0,0?").buildUpon().appendQueryParameter("q",location).build();

        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager())!= null)
        {
            startActivity(intent);
        }
        else
        {
            Log.d(LOG_TAG, "Couldn't call "+location+", no recieving app installed on the device.");
        }
    }
    /*
    @Override
    public void onStart()
    {
        Log.v(LOG_TAG,"in onStart");
        super.onStart();
    }

    @Override
    public void onResume()
    {
        Log.v(LOG_TAG,"in onResume");
        super.onResume();
    }

    @Override
    public void onPause()
    {
        Log.v(LOG_TAG,"in onPause");
        super.onPause();
    }

    @Override
    public void onStop()
    {
        Log.v(LOG_TAG,"in OnStop");
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        Log.v(LOG_TAG,"in onDestroy");
        super.onDestroy();
    }
    */
}
