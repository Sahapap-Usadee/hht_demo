
package com.jastec.hht_demo.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jastec.hht_demo.BuildConfig;
import com.jastec.hht_demo.Connection.NetworkService;
import com.jastec.hht_demo.R;

import com.jastec.hht_demo.mainmenu.MainActivity;
import com.jastec.hht_demo.model.TerTest;
import com.jastec.hht_demo.remote.IMyAPI;
import com.jastec.hht_demo.remote.RetrofitClient;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.jastec.hht_demo.remote.APIUtils.BASE_URL;

public class LoginApiActivity extends AppCompatActivity {

    public static final String BroadcastStringForAction = "checkinternet";
    private IntentFilter mIntentFilter;
    public static final String EXTRA_MESSAGE = "com.jastec.hht_demo.ui.login.MESSAGE";
    private TextView Connect_Status;
    private ImageView Connect_Image_Status;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private SharedPreferences preferences;
    ProgressBar loadingProgressBar;
    final int MIN_PASSWORD_LENGTH = 2;
    private final static int LOADING_DURATION = 2000;
    private long mLastClickTime = 0;

    private IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_api);


        viewInitializations();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkService.isOnline(getApplicationContext())) {
                performSignUp(v);
                  }
                else
                {

                    Toast.makeText(LoginApiActivity.this, "Network is available : FALSE ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // isNetworkAvailable(LoginApiActivity.this);

    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    void viewInitializations() {
        iMyAPI = RetrofitClient.getInstance(BASE_URL).create(IMyAPI.class);
        Connect_Status = findViewById(R.id.textStatus);
        Connect_Image_Status = findViewById(R.id.imageStatus);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loadingProgressBar = findViewById(R.id.loading);
        loginButton.setEnabled(true);
        final TextView PG_Version_cd = findViewById(R.id.version_cd);
        PG_Version_cd.setText("Version :" + BuildConfig.VERSION_CODE);
        preferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        if (preferences.contains("isUserLogin"))
        {
            String uname = preferences.getString("username", "username");
            String upass = preferences.getString("password", "password");
            usernameEditText.setText(uname);


        }

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, NetworkService.class);
        startService(serviceIntent);

        if(NetworkService.isOnline(getApplicationContext()))
        {
             Set_ON();
        }
        else
        {
            Set_OFF();
            Toast.makeText(LoginApiActivity.this, "Network is available : FALSE ", Toast.LENGTH_SHORT).show();

        }
    }

    public void performSignUp(View v) {
        if (validateInput()) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
            AlertDialog dialog = new SpotsDialog.Builder()
                    .setContext(LoginApiActivity.this)
                    .build();
            dialog.show();




            String txt_user = usernameEditText.getText().toString();
            String txt_password = passwordEditText.getText().toString();




            //------------------------------------
            TerTest user = new TerTest(txt_user, txt_password, "");
            compositeDisposable.add(iMyAPI.LoginUser(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {

                            Toast.makeText(LoginApiActivity.this, s, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();


                            if (s.contains("Login successfully")) //Login successfully
                            {
                                Login_Intent(txt_user,txt_password);
//
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            dialog.dismiss();
                            Toast.makeText(LoginApiActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }));
            mLastClickTime = SystemClock.elapsedRealtime();

        }
    }

private void Login_Intent(String In_user, String In_password){
        SharedPreferences preferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", In_user);
        editor.putString("password", In_password);

        editor.putBoolean("isUserLogin", true);
        editor.commit();
        loadingProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(LoginApiActivity.this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE, In_user);
        startActivity(intent);
    }

    boolean validateInput() {

        if (usernameEditText.getText().toString().equals("")) {
            usernameEditText.setError("Please Enter User Id");
            return false;
        }
        if (passwordEditText.getText().toString().equals("")) {
            passwordEditText.setError("Please Enter Password");
            return false;
        }

        // checking the proper email format


        // checking minimum password Length
        if (passwordEditText.getText().length() < MIN_PASSWORD_LENGTH) {
            passwordEditText.setError("Password Length must be more than " + MIN_PASSWORD_LENGTH + "characters");
            return false;
        }
        loginButton.setEnabled(true);

        return true;
    }


    public BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastStringForAction))
            {
                if(intent.getStringExtra("online_status").equals("true"))
                {
                    Set_ON();
                }
                else
                {
                    Set_OFF();
                }
            }
        }
    };

//    public boolean isOnline(Context context) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (connectivityManager != null) {
//
//
//            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
//            if (capabilities != null) {
//                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    return true;
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    return true;
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                    return true;
//                }
//            }
//
//
//        }
//        Toast.makeText(context, "Network is available : FALSE ", Toast.LENGTH_SHORT).show();
//        return false;
//
//    }

    public void Set_ON() {

        Connect_Status.setText("Network Connection : OK");
        Connect_Image_Status.setColorFilter(ContextCompat.getColor(this, R.color.green_500), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void Set_OFF() {
        Connect_Status.setText("Network Connection : FALSE");
        Connect_Image_Status.setColorFilter(ContextCompat.getColor(this, R.color.red_A400), android.graphics.PorterDuff.Mode.MULTIPLY);
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        registerReceiver(MyReceiver,mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(MyReceiver,mIntentFilter);
    }


//    public static boolean isNetworkAvailable(Context context) {
//        if (context == null) return false;
//
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (connectivityManager != null) {
//
//
//            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
//            if (capabilities != null) {
//                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    return true;
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    return true;
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                    return true;
//                }
//            }
//
//
//        }
//        Toast.makeText(context, "Network is available : FALSE ", Toast.LENGTH_SHORT).show();
//      //  Log.i("update_statut", "Network is available : FALSE ");
//        return false;
//    }
}