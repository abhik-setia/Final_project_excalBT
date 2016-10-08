package com.example.vinay.bluesample;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.AdapterView;

import java.util.List;
import java.util.prefs.PreferenceChangeListener;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences user_shared_pref;
    Boolean noti,alarm,flash,vibrate;
    SwitchPreference noti_pref,alarm_pref,flash_pref,vibrate_pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        ActionBar ac=getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);

        user_shared_pref=getSharedPreferences("user_pref",Context.MODE_PRIVATE);
        noti=user_shared_pref.getBoolean("notification_preference",true);
        alarm=user_shared_pref.getBoolean("alarm_preference",true);
        flash=user_shared_pref.getBoolean("flash_preference",true);
        vibrate=user_shared_pref.getBoolean("vibration_preference",true);

        PreferenceManager.setDefaultValues(this,R.xml.preferences,true);

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        //Log.v("hello", String.valueOf(sharedPreferences.getBoolean(key,false)));
        SharedPreferences.Editor editor=user_shared_pref.edit();
        editor.putBoolean(key,sharedPreferences.getBoolean(key,true));
        Log.v("hello"," set "+key+sharedPreferences.getBoolean(key,true));
        editor.commit();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}
