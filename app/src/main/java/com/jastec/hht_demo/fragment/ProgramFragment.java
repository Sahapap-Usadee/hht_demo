package com.jastec.hht_demo.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jastec.hht_demo.R;
import com.jastec.hht_demo.adapter.AdapterMenuleft;
import com.jastec.hht_demo.data.DataGenerator;
import com.jastec.hht_demo.mainmenu.MainActivity;
import com.jastec.hht_demo.model.MsPg;
import com.jastec.hht_demo.model.PgMenu;
import com.jastec.hht_demo.remote.IMyAPI;
import com.jastec.hht_demo.remote.RetrofitClient;
import com.jastec.test_dependency.TestActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jastec.hht_demo.remote.APIUtils.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProgramFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgramFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private AdapterMenuleft adapter;
    private long mLastClickTime = 0;
    List<MsPg> Program_ALl;
    IMyAPI iMyAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ProgramFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgramFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgramFragment newInstance(String param1, String param2) {
        ProgramFragment fragment = new ProgramFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iMyAPI = RetrofitClient.getInstance(BASE_URL).create(IMyAPI.class);


        // compositeDisposable.add(
//                iMyAPI.GetPg()
//                        .subscribeOn(Schedulers.io())
//
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<List<MsPg>>() {
//                            @Override
//                            public void accept(List<MsPg> msPgs) throws Exception {
//                                Program_ALl =msPgs;
//                                for (int i = 0; i < Program_ALl.size(); i++) {
//                                    //System.out.println(Program_ALl.get(i));
//                                    Toast.makeText(getActivity(), Program_ALl.get(i).getPgId(), Toast.LENGTH_SHORT).show();
//                                }
//
//
//                            }
//
//                        }, new Consumer<Throwable>() {
//                            @Override
//                            public void accept(Throwable throwable) throws Exception {
//                                // dialog.dismiss();
//                                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
        //  );


//                iMyAPI.GetPg(new Callback<List<MsPg>>() {
//                    @Override
//                    public void onResponse(Call<List<MsPg>> call, Response<List<MsPg>> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<MsPg>> call, Throwable t) {
//
//                    }
//                });


//        iMyAPI.GetPg().enqueue(new Callback<List<MsPg>>() {
//            @Override
//            public void onResponse(Call<List<MsPg>> call, Response<List<MsPg>> response) {
//
//                    Program_ALl =response.body();
//                        for (int i = 0; i < Program_ALl.size(); i++) {
//                            //System.out.println(Program_ALl.get(i));
//                            Toast.makeText(getActivity(), Program_ALl.get(i).getPgId(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<MsPg>> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        //compositeDisposable.dispose();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_program, container, false);
        // Inflate the layout for this fragment
        Observable<List<MsPg>> Program_Name_API = iMyAPI.GetPg();
        compositeDisposable.add(Program_Name_API
                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MsPg>>() {
                    @Override
                    public void accept(List<MsPg> msPgs) throws Exception {
                        Program_ALl = msPgs;
                        initComponent(rootView, container, Program_ALl);
//                        for (int i = 0; i < Program_ALl.size(); i++) {
//                            //System.out.println(Program_ALl.get(i));
//                          //  Toast.makeText(getActivity(), Program_ALl.get(i).getPgId(), Toast.LENGTH_SHORT).show();
//                        }
                        compositeDisposable.dispose();

                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        // dialog.dismiss();
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    //    compositeDisposable.dispose();
                    }
                }));
//         initComponent(rootView,container,Program_ALl);
        //   Toast.makeText(getActivity(),"Text!",Toast.LENGTH_SHORT).show();
        return rootView;
    }

    private void initComponent(View rootView, ViewGroup container, List<MsPg> pg_all) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.Program_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //set data and list adapter
        adapter = new AdapterMenuleft(getActivity(), DataGenerator.getPgMenuData(getActivity(), pg_all));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterMenuleft.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PgMenu obj, int pos) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                Toast.makeText(getActivity(), obj.pg_name, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivity(intent);
            }
        });
    }
}