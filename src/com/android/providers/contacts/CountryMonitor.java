/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License
 */

package com.android.providers.contacts;

import android.content.Context;
import android.location.Country;
import android.location.CountryDetector;
import android.location.CountryListener;
import android.os.Looper;

/**
 * This class monitors the change of country.
 * <p>
 * {@link #getCountryIso()} is used to get the ISO 3166-1 two letters country
 * code of current country.
 */
public class CountryMonitor {
    private static CountryMonitor sSingleton;
    private String mCurrentCountryIso;
    private Context mContext;

    public synchronized static CountryMonitor getInstance(Context context) {
        if (sSingleton == null) {
            sSingleton = new CountryMonitor(context);
        }
        return sSingleton;
    }

    private CountryMonitor(Context context) {
        mContext = context;
    }

    /**
     * Get the current country code
     *
     * @return the ISO 3166-1 two letters country code of current country.
     */
    public synchronized String getCountryIso() {
        if (mCurrentCountryIso == null) {
            final CountryDetector countryDetector =
                    (CountryDetector) mContext.getSystemService(Context.COUNTRY_DETECTOR);
            mCurrentCountryIso = countryDetector.detectCountry().getCountryIso();
            countryDetector.addCountryListener(new CountryListener() {
                public void onCountryDetected(Country country) {
                    synchronized (sSingleton) {
                        mCurrentCountryIso = country.getCountryIso();
                    }
                }
            }, Looper.getMainLooper());
        }
        return mCurrentCountryIso;
    }
}