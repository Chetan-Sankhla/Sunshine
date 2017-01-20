package com.sankhla.sunshine;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.SimpleTimeZone;

/**
 * Created by Chetan_Sankhla on 17-Jan-17.
 */

public class FetchWeatherTask
{
    public final static String KEY_DATE="dt";
    public final static String KEY_MIN="min";
    public final static String KEY_MAX="max";
    public final static String KEY_DESC="description";
    public final static String KEY_CITY="city";
    public final static String KEY_CITY_ID="id";
    public final static String KEY_CITY_NAME="name";
    public final static String KEY_CITY_COORDINATE="coord";
    public final static String KEY_CITY_COORDINATE_LON="lon";
    public final static String KEY_CITY_COORDINATE_LAT="lat";
    public final static String KEY_CITY_COUNTRY="country";
    public final static String KEY_CITY_POPULATION="population";
    public final static String KEY_COD="cod";
    public final static String KEY_MESSAGE="message";
    public final static String KEY_COUNT="cnt";
    public final static String KEY_LIST="list";
    public final static String KEY_TEMP="temp";
    public final static String KEY_TEMP_DAY="day";
    public final static String KEY_TEMP_NIGHT="night";
    public final static String KEY_TEMP_EVENING="eve";
    public final static String KEY_TEMP_MORNING="morn";
    public final static String KEY_PRESSURE="pressure";
    public final static String KEY_HUMIDITY="humidity";
    public final static String KEY_WEATHER="weather";
    public final static String KEY_WEATHER_ID="id";
    public final static String KEY_WEATHER_MAIN="main";
    public final static String KEY_WEATHER_ICON="icon";
    public final static String KEY_SPEED="speed";
    public final static String KEY_DEGREE="deg";
    public final static String KEY_CLOUDS="clouds";
    public final static String KEY_RAIN="rain";

    private final Context mContext;

    public FetchWeatherTask(Context context)
    {
        mContext=context;
    }
    public static ArrayList<HashMap<String,String>> parse(JSONObject response)
    {
        ArrayList<HashMap<String,String>> mainList = new ArrayList<>();
        try
        {
            JSONArray mainArray=response.getJSONArray("list");

            for (int i=0;i<mainArray.length();i++)
            {
                HashMap<String,String> item = new HashMap<>();
                JSONObject listObject=mainArray.getJSONObject(i);
                JSONObject tempObject=listObject.getJSONObject("temp");
                JSONArray wthObject=listObject.getJSONArray("weather");
                item.put(KEY_DATE,listObject.getString(KEY_DATE));
                item.put(KEY_MIN,tempObject.getString(KEY_MIN));
                item.put(KEY_MAX,tempObject.getString(KEY_MAX));
                for(int j=0;j<wthObject.length();j++){
                    JSONObject temp = wthObject.getJSONObject(j);
                    item.put(KEY_DESC,temp.getString(KEY_DESC));
                }

                mainList.add(item);
                Log.d("parseArr: ",mainList.toString());
                Log.d("parseItem: ",item.toString());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return mainList;
    }

    public String getReadableDateString(long time)
    {
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }


}
