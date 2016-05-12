package com.khyzhun.sasha.criminalintent;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab instance;
    private Context context;
    private List<Crime> crimes;

    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";

    private CrimeLab(Context appContext) {
        this.context = appContext;
        loadCrimes();
    }

    private void loadCrimes() {
        CriminalIntentSerializer crimeSerializer = new CriminalIntentSerializer(this.context, FILENAME);
        try {
            this.crimes = crimeSerializer.loadCrimes();
        } catch (Exception e) {
            crimes = new ArrayList<Crime>();
            Log.d(TAG, "There was an error loading the crimes.", e);
        }
    }

    public void addCrime(Crime crime) {
        this.crimes.add(crime);
    }

    public void deleteCrime(Crime crime) {
        this.crimes.remove(crime);
    }

    public static CrimeLab getInstance(Context appContext) {
        if (instance == null) {
            instance = new CrimeLab(appContext.getApplicationContext());
        }

        return instance;
    }

    public List<Crime> getCrimes() {
        return crimes;
    }


    public Crime getCrime(UUID id) {
        for(Crime crime : crimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }

        return null;
    }

    public boolean saveCrimes() {
        CriminalIntentSerializer crimeSerializer = new CriminalIntentSerializer(this.context, FILENAME);
        try {
            Log.d(TAG, "Saving crimes...");
            crimeSerializer.saveCrimes(this.crimes);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "There was an error saving the crimes", e);
            return false;
        }
    }
}