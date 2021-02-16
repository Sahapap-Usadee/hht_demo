package com.jastec.hht_demo.data;

import android.content.Context;
import android.content.res.TypedArray;

import com.jastec.hht_demo.model.MsPg;
import com.jastec.hht_demo.model.PgMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.jastec.hht_demo.R;

public class DataGenerator {

    public static List<PgMenu> getPgMenuData(Context ctx, List<MsPg> pg_all) {
        List<PgMenu> items = new ArrayList<>();

        // TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.program_names);
        if (pg_all == null) {
            for (int i = 0; i < name_arr.length; i++) {
                PgMenu obj = new PgMenu();
                // obj.image = drw_arr.getResourceId(i, -1);
                obj.pg_name = name_arr[i];
                //  obj.email = Tools.getEmailFromName(obj.name);
                // obj.imageDrw = ctx.getResources().getDrawable(obj.image);
                items.add(obj);
            }
        } else {


            for (int i = 0; i < pg_all.size(); i++) {
                PgMenu obj = new PgMenu();
                // obj.image = drw_arr.getResourceId(i, -1);
                obj.pg_name = pg_all.get(i).getPgName();
                obj.pg_id = pg_all.get(i).getPgId();
                //  obj.email = Tools.getEmailFromName(obj.name);
                // obj.imageDrw = ctx.getResources().getDrawable(obj.image);
                items.add(obj);
            }
            //    Collections.shuffle(items);

        }
        return items;
    }


    public static List<PgMenu> getPgMenuDataStock(Context ctx, List<MsPg> pg_all) {
        List<PgMenu> items = new ArrayList<>();

        // TypedArray drw_arr = ctx.getResources().obtainTypedArray(R.array.people_images);
        String name_arr[] = ctx.getResources().getStringArray(R.array.program_names);
        if (pg_all == null) {
            for (int i = 0; i < name_arr.length; i++) {
                PgMenu obj = new PgMenu();
                // obj.image = drw_arr.getResourceId(i, -1);
                obj.pg_name = name_arr[i];
                //  obj.email = Tools.getEmailFromName(obj.name);
                // obj.imageDrw = ctx.getResources().getDrawable(obj.image);
                items.add(obj);
            }
        } else {


            for (int i = 0; i < pg_all.size(); i++) {
                if (pg_all.get(i).getPgId().trim().equals("Stock")) {
                    PgMenu obj = new PgMenu();
                    // obj.image = drw_arr.getResourceId(i, -1);
                    obj.pg_name = pg_all.get(i).getPgName();
                    obj.pg_id = pg_all.get(i).getPgId();
                    //  obj.email = Tools.getEmailFromName(obj.name);
                    // obj.imageDrw = ctx.getResources().getDrawable(obj.image);
                    items.add(obj);
                }
            }
            //    Collections.shuffle(items);

        }
        return items;
    }
}
