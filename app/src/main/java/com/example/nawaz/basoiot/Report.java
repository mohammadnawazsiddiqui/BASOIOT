package com.example.nawaz.basoiot;

import android.os.Parcel;
import android.os.Parcelable;

class Report implements Parcelable {

    String person;
    String attendancTotal;
    String reportDetails;

    public Report(String person, String attendancTotal, String reportDetails) {
        this.person = person;
        this.attendancTotal = attendancTotal;
        this.reportDetails = reportDetails;
    }

    protected Report(Parcel in) {
        person = in.readString();
        attendancTotal = in.readString();
        reportDetails = in.readString();
    }

    public static final Creator<Report> CREATOR = new Creator<Report>() {
        @Override
        public Report createFromParcel(Parcel in) {
            return new Report(in);
        }

        @Override
        public Report[] newArray(int size) {
            return new Report[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(person);
        dest.writeString(attendancTotal);
        dest.writeString(reportDetails);
    }
}
