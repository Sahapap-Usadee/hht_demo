package com.jastec.hht_demo.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jastec.dialogalert.KAlertDialog;
import com.jastec.hht_demo.R;
import com.jastec.hht_demo.adapter.ProgramAdapter;
import com.jastec.hht_demo.data.DataGenerator;
import com.jastec.hht_demo.model.MsPg;
import com.jastec.hht_demo.model.PgMenu;
import com.jastec.hht_demo.remote.IMyAPI;
import com.jastec.hht_demo.remote.RetrofitClient;
import com.jastec.hht_demo.ui.login.LoginApiActivity;
import com.jastec.test_dependency.TestActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.jastec.hht_demo.remote.APIUtils.BASE_URL;


public class ProgramFragment extends Fragment {


    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private long mLastClickTime = 0;
    Button buttonRefease;
    List<MsPg> ProgramAll;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_program, container, false);
        iMyAPI = RetrofitClient.getInstance(BASE_URL).create(IMyAPI.class);
        initComponent(rootView, container);
        Button buttonRefresh = (Button) rootView.findViewById(R.id.btn_refresh);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setProgramAdapter(DataGenerator.getPgMenuRe(getActivity(), ProgramAll));
            }
        });
        // Inflate the layout for this fragment

        return rootView;
    }

    private void initComponent(View rootView, ViewGroup container) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.Program_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProgramAdapter(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ProgramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PgMenu obj, int pos) {
                OpenProgram(obj);
            }
        });
        OnLoadData();

    }

    private void OpenProgram(PgMenu obj) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        Toast.makeText(getActivity(), obj.pg_name, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), TestActivity.class);
        startActivity(intent);
    }

    private Observable<List<MsPg>> RxProgramAll(Observable<List<MsPg>> programAPI) {
        return programAPI.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    private void OnLoadData() {
        RxProgramAll(iMyAPI.GetPg()).subscribe(responseProgram, errorResponseProgram);
    }


    private Consumer<List<MsPg>> responseProgram = new Consumer<List<MsPg>>() {
        @Override
        public void accept(List<MsPg> msPgs) throws Exception {
            ProgramAll = msPgs;
            adapter.setProgramAdapter(DataGenerator.getPgMenuData(getActivity(), ProgramAll));
            compositeDisposable.dispose();
        }
    };

    private Consumer<Throwable> errorResponseProgram = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable throwable) throws Exception {
            // dialog.dismiss();
            Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            //    compositeDisposable.dispose();
        }
    };


}