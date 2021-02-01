package com.jastec.hht_demo.Connection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.jastec.hht_demo.ui.login.LoginApiActivity;

public class NetworkService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("inside","oncreate");


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

       throw new  UnsupportedOperationException("Not yet implement");

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            handler.post(periodUpdate);

      //  return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {


            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true;
                }

            }


        }

        return false;
    }

    Handler handler = new Handler(Looper.getMainLooper());
    private Runnable periodUpdate=new Runnable(){


        @Override
        public void run() {
            handler.postDelayed(periodUpdate,1*1000- SystemClock.elapsedRealtime()%1000);

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(LoginApiActivity.BroadcastStringForAction);
            broadcastIntent.putExtra("online_status",""+ isOnline(NetworkService.this));
            sendBroadcast(broadcastIntent);

        }
    };

  }
