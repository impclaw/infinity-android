package com.cgordon.infinityandroid.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.cgordon.infinityandroid.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_info);
    }
}
