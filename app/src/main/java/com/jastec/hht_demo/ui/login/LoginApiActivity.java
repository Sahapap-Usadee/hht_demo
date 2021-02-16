
package com.jastec.hht_demo.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jastec.dialogalert.KAlertDialog;
import com.jastec.hht_demo.BuildConfig;
import com.jastec.hht_demo.Connection.NetworkService;
import com.jastec.hht_demo.R;

import com.jastec.hht_demo.mainmenu.MainActivity;
import com.jastec.hht_demo.model.MsPg;
import com.jastec.hht_demo.model.TerTest;
import com.jastec.hht_demo.remote.IMyAPI;

//mport com.jastec.hht_demo.remote.RetrofitClient;
import com.jastec.retofitclient.remote.RetrofitClientLib;

import java.util.ArrayList;
import java.util.List;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.jastec.hht_demo.remote.APIUtils.BASE_URL;
import static com.jastec.retofitclient.remote.APIUtilsLib.BASE_URL_LIB;

public class LoginApiActivity extends AppCompatActivity {

    public static final String BroadcastStringForAction = "checkinternet";
    private IntentFilter mIntentFilter;
    public static final String EXTRA_USER_ID = "com.jastec.hht_demo.ui.login.USER_ID";
    public static final String EXTRA_PROGRAM_ALL = "com.jastec.hht_demo.ui.login.EXTRA_PROGRAM_ALL";
    public static final String NetworkFalse = "Network is available : FALSE";
    public static final String LoginPref = "login_pref";
    public static final String LoginSuccess = "Login successfully";
    public static final String PrefIsUserLogin = "isUserLogin";
    public static final String PrefUsername = "username";
    public static final String PrefPassword = "password";
    public static final String OnlineStatus ="online_status";
    String txtUser;
    String txtPassword;
    private TextView ConnectStatus;
    private ImageView ConnectImageStatus;
    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private SharedPreferences preferences;
     KAlertDialog pDialog;
    final int MIN_PASSWORD_LENGTH = 2;
    //  private final static int LOADING_DURATION = 2000;
    private long mLastClickTime = 0;
    //  List<TerTest> ter_TEST = new ArrayList<>();
    private IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Observable<List<MsPg>> ProgramMenuAPI;
    ArrayList<MsPg> Program_All = new ArrayList<MsPg>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_api);
//        for (int i=0;i<6;i++)
//        {
//
//            ter_TEST.add(new TerTest("aasxz","baa","caa"));

        viewInitializations();
        NetworkServiceStart();
        loginButtonClick();
        // isNetworkAvailable(LoginApiActivity.this);

    }


    private void loginButtonClick() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkService.isOnline(getApplicationContext())) {
                    performSignUp(v);
                } else {
                    MsgErrorType(NetworkFalse);
                    Toast.makeText(LoginApiActivity.this, NetworkFalse, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStop() {
        //  compositeDisposable.clear();
        super.onStop();
        Toast.makeText(LoginApiActivity.this, "Destroy", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void viewInitializations() {

        iMyAPI = RetrofitClientLib.getInstance(BASE_URL).create(IMyAPI.class);
        ProgramMenuAPI = iMyAPI.GetPg();
        ConnectStatus = findViewById(R.id.textStatus);
        ConnectImageStatus = findViewById(R.id.imageStatus);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);

        loginButton.setEnabled(true);
        final TextView PG_Version_cd = findViewById(R.id.version_cd);
        PG_Version_cd.setText("Version :" + BuildConfig.VERSION_CODE);

        preferences = getSharedPreferences(LoginPref, Context.MODE_PRIVATE);
        if (preferences.contains(PrefIsUserLogin)) {
            String pre_userName = preferences.getString(PrefUsername, "username");
            String pre_userPassword = preferences.getString(PrefPassword, "password");
            usernameEditText.setText(pre_userName);
        }


    }

    private void NetworkServiceStart() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadcastStringForAction);
        Intent serviceIntent = new Intent(this, NetworkService.class);
        startService(serviceIntent);

        if (NetworkService.isOnline(getApplicationContext())) {
            Set_ON();
        } else {
            Set_OFF();
            MsgErrorType(NetworkFalse);
            Toast.makeText(LoginApiActivity.this, NetworkFalse, Toast.LENGTH_SHORT).show();
        }
    }

    public BroadcastReceiver MyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastStringForAction)) {
                if (intent.getStringExtra(OnlineStatus).equals("true")) {
                    Set_ON();
                } else {
                    Set_OFF();
                }
            }
        }
    };

    public void performSignUp(View v) {
        if (validateInput()) {

            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
//            AlertDialog dialog = new SpotsDialog.Builder()
//                    .setContext(LoginApiActivity.this)
//                    .build();
//            dialog.show();

            //ProgressDialog loadingDialog = ProgressDialog.show(LoginApiActivity.this, "Sign In", "Loading...", true, false);
            SetUpDialogLoad();

            txtUser = usernameEditText.getText().toString();
             txtPassword = passwordEditText.getText().toString();
            //------------------------------------
            TerTest user = new TerTest(txtUser, txtPassword, "");
            CheckSignUpData(user);
//            Program_Name_API
//                    .subscribeOn(Schedulers.io())
//
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<List<MsPg>>() {
//                        @Override
//                        public void accept(List<MsPg> msPgs) throws Exception {
//                            Program_ALl = msPgs;
//                            // initNavigationMenuData();
//
//                            // compositeDisposable.dispose();
//                        }
//
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            // dialog.dismiss();
//                            Toast.makeText(LoginApiActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                            Log.d("error failure", throwable.getMessage());
//                            //    compositeDisposable.dispose();
//                        }
//                    });

//            compositeDisposable.add(iMyAPI.GetUSER_COUNT(ter_TEST)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<String>() {
//                        @Override
//                        public void accept(String s) throws Exception {
//
//                            Toast.makeText(LoginApiActivity.this, s, Toast.LENGTH_SHORT).show();
//                            dialog.dismiss();
//
//
//
//                        }
//                    }, new Consumer<Throwable>() {
//                        @Override
//                        public void accept(Throwable throwable) throws Exception {
//                            dialog.dismiss();
//                            Toast.makeText(LoginApiActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//
//                        }
//                    }));
            mLastClickTime = SystemClock.elapsedRealtime();

        }
    }

    private void SetUpDialogLoad() {
        pDialog = new KAlertDialog(LoginApiActivity.this, KAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Loading");
        pDialog.show();
        pDialog.setCancelable(false);
    }


    private Observable<String> RxLoginUser(Observable<String> loginUser) {

        return loginUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private void CheckSignUpData(TerTest user) {

        compositeDisposable.add(RxLoginUser(iMyAPI.LoginUser(user)).subscribe(responseUser,errorResponseUser));

    }


    private Consumer<String> responseUser = new Consumer<String>() {
        @Override
        public void accept(String s) throws Exception {
            // TODO:
            if (s.contains(LoginSuccess)) //Login successfully
            {
                GetProgramMenu();
                //  Login_Intent(txt_user, txt_password);
                // compositeDisposable.clear();
            } else {
                onFinishDialog(pDialog, s, txtUser, txtPassword, KAlertDialog.ERROR_TYPE);
                // loadingDialog.dismiss();
                //  dialog.dismiss();
            }
            Toast.makeText(LoginApiActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    };

    private Consumer<Throwable> errorResponseUser = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            onFinishDialog(pDialog, throwable.getMessage(), txtUser, txtPassword, KAlertDialog.ERROR_TYPE);
            // loadingDialog.dismiss();
            //dialog.dismiss();
            Toast.makeText(LoginApiActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };


    private void GetProgramMenu() {
        compositeDisposable.add(RxProgramMenu(ProgramMenuAPI).subscribe(responseProgramMenu, errorResponseProgramMenu));
    }

    private Observable<List<MsPg>> RxProgramMenu(Observable<List<MsPg>> program_name_api) {
        return program_name_api.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Consumer<List<MsPg>> responseProgramMenu = new Consumer<List<MsPg>>() {
        @Override
        public void accept(List<MsPg> msPgs) throws Exception {

            if (msPgs != null) {
                Program_All = (ArrayList<MsPg>) msPgs;
                // initNavigationMenuData();
                //    Login_Intent(txt_user, txt_password);
                onFinishDialog(pDialog, LoginSuccess, txtUser, txtPassword, KAlertDialog.SUCCESS_TYPE);
                // compositeDisposable.dispose();
            } else {
                Toast.makeText(LoginApiActivity.this, "No Program to select", Toast.LENGTH_SHORT).show();
                onFinishDialog(pDialog, "No Program to select", txtUser, txtPassword, KAlertDialog.ERROR_TYPE);
            }
            //  loadingDialog.dismiss();

            //dialog.dismiss();
        }
    };


    private Consumer<Throwable> errorResponseProgramMenu = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            //loadingDialog.dismiss();
            onFinishDialog(pDialog, throwable.getMessage(), txtUser, txtPassword, KAlertDialog.ERROR_TYPE);
            //  dialog.dismiss();
            Toast.makeText(LoginApiActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("error failure", throwable.getMessage());
            //    compositeDisposable.dispose();

        }
    };


    public void onFinishDialog(KAlertDialog Dialog, String pTitle, String pUser, String pPassword, int AlertType) {

        Dialog.setTitleText(pTitle)
                .setConfirmText("OK")
                .changeAlertType(AlertType);
        if (AlertType == KAlertDialog.SUCCESS_TYPE) {
            Dialog.setConfirmClickListener(sDialog -> Login_Intent(pUser, pPassword));
        } else {
            //  pDialog.setCanceledOnTouchOutside(true);
        }
    }

    private void Login_Intent(String In_user, String In_password) {

        SharedPreferences preferences = getSharedPreferences(LoginPref, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PrefUsername, In_user);
        editor.putString(PrefPassword, In_password);
        editor.putBoolean(PrefIsUserLogin, true);
        editor.commit();

        Intent intent = new Intent(LoginApiActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_PROGRAM_ALL, Program_All);
        intent.putExtras(bundle);

        intent.putExtra(EXTRA_USER_ID, In_user);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);


    }


    private void MsgErrorType(String txtTitle) {
        KAlertDialog eDialog = new KAlertDialog(this, KAlertDialog.ERROR_TYPE);
        eDialog.setCanceledOnTouchOutside(true);
        eDialog.setTitleText("LoginActivity")
                .setContentText(txtTitle)
                .setCancelText("OK")
                .show();
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


        if (passwordEditText.getText().length() < MIN_PASSWORD_LENGTH) {
            passwordEditText.setError("Password Length must be more than " + MIN_PASSWORD_LENGTH + "characters");
            return false;
        }
        loginButton.setEnabled(true);

        return true;
    }


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

        ConnectStatus.setText("Network Connection : OK");
        ConnectImageStatus.setColorFilter(ContextCompat.getColor(this, R.color.green_500), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void Set_OFF() {
        ConnectStatus.setText("Network Connection : FALSE");
        ConnectImageStatus.setColorFilter(ContextCompat.getColor(this, R.color.red_A400), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(MyReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(MyReceiver, mIntentFilter);
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