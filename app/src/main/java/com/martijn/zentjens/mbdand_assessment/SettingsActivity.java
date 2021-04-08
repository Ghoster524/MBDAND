package com.martijn.zentjens.mbdand_assessment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener
    {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_main);

            updateSummary(getPreferenceScreen());

            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        private String updateSummary(Preference p)
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(p.getContext());
            p.setSummary("");
            if (p instanceof EditTextPreference)
            {
                p.setSummary(preferences.getString(p.getKey(), ""));
            }
            else if (p instanceof ListPreference)
            {
                String value = preferences.getString(p.getKey(), "");
                int index = ((ListPreference) p).findIndexOfValue(value);
                if (index >= 0)
                {
                    p.setSummary(((ListPreference) p).getEntries()[index]);
                }
            }
            else if (p instanceof MultiSelectListPreference)
            {
                Set<String> values = preferences.getStringSet(p.getKey(), Collections.<String>emptySet());
                for (String value : values)
                {
                    int index = ((MultiSelectListPreference) p).findIndexOfValue(value);
                    if (index >= 0)
                    {
                        p.setSummary(p.getSummary() + (p.getSummary().length() == 0 ? ""
                                : ", ") + ((MultiSelectListPreference) p).getEntries()[index]);
                    }
                }
            }
            else if (p instanceof PreferenceCategory)
            {
                PreferenceCategory preference = (PreferenceCategory) p;
                for (int i = 0; i < preference.getPreferenceCount(); i++)
                {
                    updateSummary(preference.getPreference(i));
                }
            }
            else if (p instanceof PreferenceGroup)
            {
                PreferenceGroup preference = (PreferenceGroup) p;
                for (int i = 0; i < preference.getPreferenceCount(); i++)
                {
                    p.setSummary(p.getSummary() + (p.getSummary().length() == 0 ? ""
                            : ", ") + updateSummary(preference.getPreference(i)));
                }
            }
            return p.getSummary().toString();
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            updateSummary(getPreferenceScreen());
        }
    }
}
