package com.jastec.hht_demo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MsPg {
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
}
