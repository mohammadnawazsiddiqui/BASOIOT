package com.example.nawaz.basoiot.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Attendance implements Parcelable {
    String intime;

    protected Attendance(Parcel in) {
        intime = in.readString();
    }

    public static final Creator<Attendance> CREATOR = new Creator<Attendance>() {
        @Override
        public Attendance createFromParcel(Parcel in) {
            return new Attendance(in);
        }

        @Override
        public Attendance[] newArray(int size) {
            return new Attendance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(intime);
    }
}
