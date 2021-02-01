package com.jastec.hht_demo.model;

import android.graphics.drawable.Drawable;



public class PgMenu {



    public String pg_id ;

    private String pgName ;
    public String pg_name;

    public boolean section = false;

    public PgMenu() {
    }

    public PgMenu(String name,String id) {
        this.pg_name = name;
        this.pg_id = id;
    }


}
