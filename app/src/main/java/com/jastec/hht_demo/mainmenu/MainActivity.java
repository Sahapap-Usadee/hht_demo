package com.jastec.hht_demo.mainmenu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.jastec.hht_demo.R;
import com.jastec.hht_demo.adapter.ExpandableRecyclerAdapter;
import com.jastec.hht_demo.adapter.MainMenuAdapter;

import com.jastec.hht_demo.fragment.ProgramFragment;
import com.jastec.hht_demo.model.MenuType;
import com.jastec.hht_demo.model.MsPg;
import com.jastec.hht_demo.remote.IMyAPI;
import com.jastec.hht_demo.remote.RetrofitClient;
import com.jastec.hht_demo.ui.login.LoginApiActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.jastec.hht_demo.remote.APIUtils.BASE_URL;

//import com.jastec.testlibrary.ToasterMessage;
//import com.jastec.test_dependency.R.xml;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.jastec.hht_demo.ui.login.MESSAGE";
    private ActionBar actionBar;
    private String message;
    private RecyclerView recycler;
    private MainMenuAdapter adapter;


    private NavigationView nav_view;
    private View headerView;
    private Menu menuNav;
    private DrawerLayout drawer;


    private long mLastClickTime = 0;
    private int Check_NavigationMenu = 0;
    final Fragment ProgramFragment = new ProgramFragment();
    final FragmentManager fm = getSupportFragmentManager();

    Fragment active = ProgramFragment;
    private Toolbar toolbar;
    List<MsPg> Program_ALl;
    IMyAPI iMyAPI;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    List<MainMenuAdapter.ListItem> items = new ArrayList<>();
    private int Menu_Home_Id = 1;
    private String Menu_Home_Title = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        message = intent.getStringExtra(EXTRA_MESSAGE);
        iMyAPI = RetrofitClient.getInstance(BASE_URL).create(IMyAPI.class);
        Observable<List<MsPg>> Program_Name_API = iMyAPI.GetPg();


        initToolbar();
        initNavigationHead();
       // initNavigationMenu();
        recycler = (RecyclerView) findViewById(R.id.Program_recyclerView);
        adapter = new MainMenuAdapter(this,items, new MainMenuAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int itemId, String itemText) {

            }
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.setAdapter(adapter);
        compositeDisposable.add(Program_Name_API
                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MsPg>>() {
                    @Override
                    public void accept(List<MsPg> msPgs) throws Exception {
                        Program_ALl = msPgs;
                        initNavigationMenu();
                        compositeDisposable.dispose();
                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // dialog.dismiss();
                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        //    compositeDisposable.dispose();
                    }
                }));
        //  initNavigationMenu();

        //  initComponent();
        //   MenuItem Menu_home = menuNav.findItem(R.id.nav_home);
        // Set_MenuCilck(Menu_home);
    }

    private void initToolbar() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Home");

//
//        compositeDisposable.add(iMyAPI.GetPg()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<List<MsPg>>() {
//                    @Override
//                    public void accept(List<MsPg> msPgs) throws Exception {
//                        Program_ALl =msPgs;
//                        for (int i = 0; i < Program_ALl.size(); i++) {
//                            //System.out.println(Program_ALl.get(i));
//                            Toast.makeText(MainActivity.this, Program_ALl.get(i).getPgId(), Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        // dialog.dismiss();
//                        Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }));


//or can use this

//        compositeDisposable.add(iMyAPI.GetPg()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<List<MsPg>>() {
//                    @Override
//                    public void onNext(@NonNull List<MsPg> msPgs) {
//                        Toast.makeText(MainActivity.this, "ok" + msPgs.get(1).getPgId(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Toast.makeText(MainActivity.this, "complete", Toast.LENGTH_SHORT).show();
//                    }
//
//
//                }));
    }

    private void initNavigationHead() {


//        nav_view = (NavigationView) findViewById(R.id.nav_view);
//        final TextView textView_drawer_username = (TextView) headerView.findViewById(R.id.drawer_username);
//        final TextView textView_drawer_email = (TextView) headerView.findViewById(R.id.drawer_email);
//        headerView = nav_view.getHeaderView(0);
//       menuNav = nav_view.getMenu();
//        MenuItem Menu_home = menuNav.findItem(R.id.nav_home);

        final TextView textView_drawer_username = (TextView) findViewById(R.id.drawer_username);
        final TextView textView_drawer_email = (TextView) findViewById(R.id.drawer_email);
        textView_drawer_username.setText(message);
        textView_drawer_email.setText(message + "@mail.com");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
//
//        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(final MenuItem item) {
//
//                return Set_MenuCilck(item);
//            }
        //});


    }

    private void initNavigationMenu() {

        recycler = (RecyclerView) findViewById(R.id.Program_recyclerView);
        adapter = new MainMenuAdapter(this, generateMenuItems(Program_ALl), new MainMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int itemId, String itemText) {
                onMenuItemSelected(itemId, itemText);
                Toast.makeText(getApplicationContext(), "Menu_id: " + itemId, Toast.LENGTH_SHORT).show();
            }
        });

        adapter.setMode(ExpandableRecyclerAdapter.MODE_ACCORDION);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setNestedScrollingEnabled(false);
        recycler.setAdapter(adapter);

        fm.beginTransaction().add(R.id.content_main, ProgramFragment, "1").hide(ProgramFragment).commit();
        onMenuItemSelected(Menu_Home_Id, Menu_Home_Title);
        //  Set_MenuCilck(Menu_home);
        drawer.openDrawer(GravityCompat.START);

    }

    private void onMenuItemSelected(int itemId, String itemText) {

        if (Check_NavigationMenu == itemId) {
            return;
        }
        switch (itemId) {
            // Bottom Navigation -------------------------------------------------------------------

            case 401:
                Toast.makeText(getApplicationContext(), itemText, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, LoginApiActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                return;
            case 205:
                fm.beginTransaction().hide(active).show(ProgramFragment).commit();
                active = ProgramFragment;
                break;
            default:

                fm.beginTransaction().hide(active).hide(ProgramFragment).commit();
                active = ProgramFragment;
                // Buttons -----------------------------------------------------------------------------

        }
        Check_NavigationMenu = itemId;
        actionBar.setTitle(itemText);
        drawer.closeDrawers();
    }

//    private Boolean Set_MenuCilck(MenuItem item) {
//
//        int id = item.getItemId();
//        if (Check_NavigationMenu == id) {
//            return true;
//        } else if (id == R.id.nav_logout) {
//            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(MainActivity.this,
//                    LoginApiActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//
//
//            return true;
//
//        } else if (id == R.id.nav_programmenu) {
//            fm.beginTransaction().hide(active).show(ProgramFragment).commit();
//            active = ProgramFragment;
//        } else {
//            fm.beginTransaction().hide(active).hide(ProgramFragment).commit();
//            active = ProgramFragment;
//        }
//
//        //  Toast.makeText(getApplicationContext(), item.getTitle() + " Selected", Toast.LENGTH_SHORT).show();
//        //ToasterMessage.show_message(MainActivity.this,item.getTitle().toString());
//
//        Check_NavigationMenu = id;
//        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
//        actionBar.setTitle(item.getTitle());
//        drawer.closeDrawers();
//        return true;
//
//    }

    private List<MainMenuAdapter.ListItem> generateMenuItems(List<MsPg> program_ALl) {


        items.add(new MainMenuAdapter.ListItem(1, "Home", R.drawable.ic_home, MenuType.NORMAL));


        items.add(new MainMenuAdapter.ListItem(-1, "Application", -1, MenuType.DIVIDER));
        items.add(new MainMenuAdapter.ListItem(100, "Program Master", R.drawable.ic_whatshot, MenuType.HEADER));
        if (program_ALl != null) {
            for (int i = 0; i < Program_ALl.size(); i++) {
                items.add(new MainMenuAdapter.ListItem(101 + i, Program_ALl.get(i).getPgName().trim(), -2, MenuType.SUB_HEADER));
            }
//                            //System.out.println(Program_ALl.get(i));
//                          //  Toast.makeText(getActivity(), Program_ALl.get(i).getPgId(), Toast.LENGTH_SHORT).show();
//                        }
//            items.add(new MainMenuAdapter.ListItem(101, "Master1", -1, MenuType.SUB_HEADER));
//            items.add(new MainMenuAdapter.ListItem(102, "Master2", -1, MenuType.SUB_HEADER));
//            items.add(new MainMenuAdapter.ListItem(103, "Master3", -1, MenuType.SUB_HEADER));
//            items.add(new MainMenuAdapter.ListItem(104, "Master4", -1, MenuType.SUB_HEADER));
//            items.add(new MainMenuAdapter.ListItem(105, "Master5", -1, MenuType.SUB_HEADER));
//            items.add(new MainMenuAdapter.ListItem(106, "Master6", -1, MenuType.SUB_HEADER));
//            items.add(new MainMenuAdapter.ListItem(107, "Master7", -1, MenuType.SUB_HEADER));
        }
        items.add(new MainMenuAdapter.ListItem(200, "Program Sub", R.drawable.ic_whatshot, MenuType.HEADER));
        items.add(new MainMenuAdapter.ListItem(201, "Sub1", -1, MenuType.SUB_HEADER));
        items.add(new MainMenuAdapter.ListItem(202, "Sub2", -1, MenuType.SUB_HEADER));
        items.add(new MainMenuAdapter.ListItem(203, "Sub3", -1, MenuType.SUB_HEADER));
        items.add(new MainMenuAdapter.ListItem(204, "Sub4", -1, MenuType.SUB_HEADER));

        items.add(new MainMenuAdapter.ListItem(205, "Program All", R.drawable.ic_whatshot, MenuType.NORMAL));
        items.add(new MainMenuAdapter.ListItem(-1, "About", -1, MenuType.DIVIDER));
        items.add(new MainMenuAdapter.ListItem(301, "About", R.drawable.ic_help, MenuType.NORMAL));

        items.add(new MainMenuAdapter.ListItem(401, "Log Out", R.drawable.exit_24px, MenuType.NORMAL));


        return items;
    }


//    private void initComponent() {
//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        //set data and list adapter
//        adapter = new AdapterMenuleft(this, DataGenerator.getPgMenuData(this));
//        recyclerView.setAdapter(adapter);
//        adapter.setOnItemClickListener(new AdapterMenuleft.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, PgMenu obj, int pos) {
//                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
//                    return;
//                }
//                mLastClickTime = SystemClock.elapsedRealtime();
//
//                Toast.makeText(getApplicationContext(), obj.name, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, TestActivity.class);
//                       startActivity(intent);
//            }
//        });
//    }
    //by ter

    @Override

    public void onBackPressed() {
//        Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        startActivity(setIntent);

        //super.onBackPressed();  // disable this
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawers();

            } else {
                exitByBackKey();
            }
            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    protected void exitByBackKey() {

        //AlertDialog alertbox =
        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        finishAffinity();

                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }
}