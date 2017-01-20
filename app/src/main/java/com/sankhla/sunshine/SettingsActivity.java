package com.sankhla.sunshine;

import android.app.Activity;
import android.os.Bundle;



/**
 * Created by Chetan_Sankhla on 16-Jan-17.
 */

public class SettingsActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,new SettingsFragment()).commit();
    }
}