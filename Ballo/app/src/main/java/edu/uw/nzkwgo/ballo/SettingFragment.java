package edu.uw.nzkwgo.ballo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by iguest on 5/31/17.
 */

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
