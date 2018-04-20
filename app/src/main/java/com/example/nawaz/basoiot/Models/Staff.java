package com.example.nawaz.basoiot.Models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;


public class Staff implements Parcelable {

    public String name;
    public String email;
    public String department;
    public String DOJ;
    public String subject;
    public String mobile;
    public String address;

    public Staff() {
    }

    public Staff(String name, String email, String department, String DOJ, String subject, String mobile, String address) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.DOJ = DOJ;
        this.subject = subject;
        this.mobile = mobile;
        this.address = address;
    }


    protected Staff(Parcel in) {
        name = in.readString();
        email = in.readString();
        department = in.readString();
        DOJ = in.readString();
        subject = in.readString();
        mobile = in.readString();
        address = in.readString();
    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(department);
        dest.writeString(DOJ);
        dest.writeString(subject);
        dest.writeString(mobile);
        dest.writeString(address);
    }
}
