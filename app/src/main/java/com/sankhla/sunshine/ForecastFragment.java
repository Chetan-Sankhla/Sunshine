package com.sankhla.sunshine;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chetan_Sankhla on 10-Jan-17.
 */

public class ForecastFragment extends Fragment implements Response.Listener<JSONObject>,Response.ErrorListener
{
    private static final String url="http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid="+BuildConfig.API_KEY;
    ForecastAdapter mForecastAdapter;
    private static final int FORECAST_LOADER=0;
    RecyclerView recyclerView;
    ArrayList<HashMap<String,String>> mainList;
    /*private static final String[] FORECAST_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.
    static final int COL_WEATHER_ID = 0;
    static final int COL_WEATHER_DATE = 1;
    static final int COL_WEATHER_DESC = 2;
    static final int COL_WEATHER_MAX_TEMP = 3;
    static final int COL_WEATHER_MIN_TEMP = 4;
    static final int COL_LOCATION_SETTING = 5;
    static final int COL_WEATHER_CONDITION_ID = 6;
    static final int COL_COORD_LAT = 7;
    static final int COL_COORD_LONG = 8;
*/
    public ForecastFragment()
    {
        //Empty Constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainList=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String locationSetting=Utility.getPreferredLocation(getActivity());
        //String sortOrder=WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        //Uri weatherForLocationUri=WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting,System.currentTimeMillis());
        //Cursor cursor=getActivity().getContentResolver().query(weatherForLocationUri,null,null,null,sortOrder);

        View view=inflater.inflate(R.layout.fragment_main,container,false);

        recyclerView= (RecyclerView) view.findViewById(R.id.recyclerview_forecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mForecastAdapter=new ForecastAdapter(getActivity());
        recyclerView.setAdapter(mForecastAdapter);

        FetchData();
        return view;
    }
    //creating menu options
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.forcast_fragment,menu);
    }
    //menu item selected events
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if (id==R.id.action_refresh)
        {
            SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
            String location=preferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
            //updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //Volley methods to fetch data from api of open weather map.
    public void FetchData()
    {
        JsonObjectRequest objectRequest=new JsonObjectRequest(Request.Method.GET,url, null, this,this);
        NetworkManager.getInstance(getActivity()).addToRequestQueue(objectRequest);

    }

    public void FetchData(String location)
    {

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("onErrorResponse: ",error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        mainList=FetchWeatherTask.parse(response);
        mForecastAdapter.addData(mainList);
        mForecastAdapter.notifyDataSetChanged();
    }
}
