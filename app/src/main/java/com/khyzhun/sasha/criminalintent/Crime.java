package com.khyzhun.sasha.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID id;
    private String title;
    private Date mDate;
    private boolean solved;
    private String mSuspect;
    private Photo mPhoto;

    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_SUSPECT = "suspect";
    private static final String JSON_PHOTO = "photo";

    public Crime() {
        id = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(JSONObject json) throws JSONException {
        this.id = UUID.fromString(json.getString(JSON_ID));
        this.title = json.getString(JSON_TITLE);
        this.solved = json.getBoolean(JSON_SOLVED);
        this.mDate = new Date(json.getLong(JSON_DATE));
        if (json.has(JSON_PHOTO))
            mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
        if (json.has(JSON_SUSPECT))
        this.mSuspect = json.getString(JSON_SUSPECT);
    }

    public UUID getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public void setPhoto(Photo p) {
        mPhoto = p;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    @Override
    public String toString() {
        return title;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, this.id.toString());
        jsonObject.put(JSON_TITLE, this.title);
        jsonObject.put(JSON_SOLVED, this.solved);
        jsonObject.put(JSON_DATE, this.mDate.getTime());

        return jsonObject;
    }
}
