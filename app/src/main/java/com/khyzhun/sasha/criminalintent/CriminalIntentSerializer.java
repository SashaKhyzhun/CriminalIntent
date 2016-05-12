package com.khyzhun.sasha.criminalintent;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CriminalIntentSerializer {

    private Context context;
    private String fileName;

    public CriminalIntentSerializer(Context context, String fileName) {
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
        List<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;

        try {
            InputStream inputStream = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            JSONArray jsonArray = readCrimesFromFile(reader);

            populateCrimes(crimes, jsonArray);
        } finally {
            if (reader != null) {
                reader.close();
            }
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