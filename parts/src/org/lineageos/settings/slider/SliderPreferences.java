/*
 * Copyright (C) 2019 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings.slidersettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SliderPreferences {

    private static final String TAG = "SliderUtils";
    private static final boolean DEBUG = false;
    private static final String SLIDER_STATUS_KEY = "slider_status";
    private static final boolean SLIDER_STATUS_DEFAULT_VALUE = true;
    private static final String SOUND_EFFECT_KEY = "slider_sound_effect";
    private static final String SOUND_EFFECT_DEFAULT_VALUE = "0";
    private SharedPreferences mSharedPrefs;

    public SliderPreferences(Context context) {
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String getSoundEffect() {
        return mSharedPrefs.getString(SOUND_EFFECT_KEY, SOUND_EFFECT_DEFAULT_VALUE);
    }

    public boolean isSliderAllowed() {
        return mSharedPrefs.getBoolean(SLIDER_STATUS_KEY, SLIDER_STATUS_DEFAULT_VALUE);
    }
}