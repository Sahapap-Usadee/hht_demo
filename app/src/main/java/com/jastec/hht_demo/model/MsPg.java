package com.jastec.hht_demo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MsPg implements Parcelable {
    @SerializedName("pgId")
    @Expose
        private String pgId ;
    @SerializedName("pgName")
    @Expose
      private String pgName ;

    public MsPg() {

    }
    public MsPg(String pgid, String pgname) {
        pgId = pgid;
        pgName = pgname;
    }

    public String getPgId() {
        return pgId;
    }

    public void setPgId(String pgid) {
        pgId = pgid;
    }

    public String getPgName() {
        return pgName;
    }

    public void setPgName(String pgname) {
        pgName = pgname;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int arg1) {
        // TODO Auto-generated method stub
        dest.writeString(pgId);
        dest.writeString(pgName);
    }


    public MsPg(Parcel in) {
        pgId = in.readString();
        pgName = in.readString();
    }

    public static final Parcelable.Creator<MsPg> CREATOR = new Parcelable.Creator<MsPg>() {
        public MsPg createFromParcel(Parcel in) {
            return new MsPg(in);
        }

        public MsPg[] newArray(int size) {
            return new MsPg[size];
        }
    };

}
