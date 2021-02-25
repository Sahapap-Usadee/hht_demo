package com.jastec.hht_demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jastec.hht_demo.R;
import com.jastec.hht_demo.adapter.ProgramAdapter;
import com.jastec.hht_demo.data.DataGenerator;
import com.jastec.hht_demo.model.MsPg;
import com.jastec.hht_demo.model.PgMenu;
import com.jastec.hht_demo.remote.IMyAPI;
import com.jastec.hht_demo.remote.RetrofitClient;
import com.jastec.stock.Activity.StockActivity;
import com.jastec.test_dependency.TestActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.jastec.hht_demo.remote.APIUtils.BASE_URL;


public class ProgramStockFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView recyclerView;
    private ProgramAdapter adapter;
    private long mLastClickTime = 0;
    List<MsPg> ProgramAll;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ProgramStockFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        iMyAPI = RetrofitClient.getInstance(BASE_URL).create(IMyAPI.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_program, container, false);
        initComponent(rootView, container);
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
        Bundle bundle = new Bundle();
        bundle.putString("formName", obj.pg_name.trim());
        Intent intentBundle = new Intent(getActivity(), StockActivity.class);
        intentBundle.putExtras(bundle);
        startActivity(intentBundle);

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
            adapter.setProgramAdapter(DataGenerator.getPgMenuDataStock(getActivity(), ProgramAll));
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