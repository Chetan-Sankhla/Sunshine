package com.sankhla.sunshine;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Chetan_Sankhla on 20-Jan-17.
 */

public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }
}
