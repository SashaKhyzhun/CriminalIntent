package com.khyzhun.sasha.criminalintent;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sasha on 22.08.15.
 */
public class CriminalIntentJSONSerializer {

    private Context context;
    private String fileName;

    public CriminalIntentJSONSerializer(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void saveCrimes(List<Crime> crimes) throws IOException, JSONException {
        JSONArray jsonArray = convertCrimesToJSON(crimes);
        writeCrimesToFile(jsonArray);
    }

    private void writeCrimesToFile(JSONArray jsonArray) throws IOException {
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private JSONArray convertCrimesToJSON(List<Crime> crimes) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (Crime crime : crimes) {
            jsonArray.put(crime.toJSON());
        }
        return jsonArray;
    }

    public List<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        try {
            // Открытие и чтение файла в StringBuilder
            InputStream in = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                // Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            // Разбор JSON с использованием JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString())
                    .nextValue();
            // Построение массива объектов Crime по данным JSONObject
            for (int i = 0; i < array.length(); i++) {
                crimes.add(new Crime(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e) {
            // Происходит при начале "с нуля"; не обращайте внимания
        } finally {
            if (reader != null)
                reader.close();
        }
        return crimes;
    }

    private void populateCrimes(List<Crime> crimes, JSONArray jsonArray) throws JSONException {
        for (int i=0; i<jsonArray.length(); i++) {
            Crime crime = new Crime(jsonArray.getJSONObject(i));
            crimes.add(crime);
        }
    }

    private JSONArray readCrimesFromFile(BufferedReader reader) throws IOException, JSONException {
        StringBuilder builder = new StringBuilder();
        String line = null;

        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return (JSONArray) new JSONTokener(builder.toString()).nextValue();
    }
}
